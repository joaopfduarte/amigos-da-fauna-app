package com.app.amigos_da_fauna.ui.screen.map

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.app.amigos_da_fauna.ui.components.ErrorBanner
import com.app.amigos_da_fauna.ui.theme.FaunaTheme
import com.app.amigos_da_fauna.util.LocationUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val defaultLat = uiState.locations.firstOrNull()?.location?.latitude ?: -14.235
    val defaultLng = uiState.locations.firstOrNull()?.location?.longitude ?: -51.925

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(defaultLat, defaultLng), 4f)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (!granted) {
            viewModel.setLocationError("Permissão de localização negada.")
            Toast.makeText(context, "Permissão de localização negada.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!LocationUtils.hasLocationPermission(context)) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= layoutInfo.totalItemsCount - 2
        }.collect { shouldLoadMore ->
            if (shouldLoadMore) viewModel.loadMore()
        }
    }

    fun requestUserLocation() {
        if (!LocationUtils.hasLocationPermission(context)) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
            viewModel.setLocationError("Permissão de localização negada.")
            return
        }
        viewModel.setLocationLoading(true)
        scope.launch {
            runCatching { LocationUtils.getCurrentLocation(context) }
                .onSuccess { location ->
                    viewModel.setUserLocation(location.latitude, location.longitude)
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude),
                            8f,
                        ),
                    )
                }
                .onFailure {
                    viewModel.setLocationError(
                        "Não foi possível obter sua localização. Verifique se o GPS está ativo.",
                    )
                }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Localização dos Animais",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colors.text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        uiState.error?.let { ErrorBanner(message = it, onRetry = { viewModel.refresh() }) }
        uiState.locationError?.let {
            ErrorBanner(message = it, onRetry = { requestUserLocation() })
        }

        Text("Lista de Animais", fontWeight = FontWeight.Bold, color = colors.text)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(uiState.locations, key = { it.id }) { item ->
                Card(colors = CardDefaults.cardColors(containerColor = colors.cardAlt)) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp),
                            contentScale = ContentScale.Crop,
                        )
                        Column {
                            Text(item.name, fontWeight = FontWeight.Bold, color = colors.text)
                            Text(item.locationDescription, fontSize = 12.sp, color = colors.textSecondary)
                        }
                    }
                }
            }
            if (uiState.isLoading) {
                item {
                    CircularProgressIndicator(color = colors.primary, modifier = Modifier.padding(8.dp))
                }
            }
        }

        GoogleMap(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState,
        ) {
            uiState.locations.forEach { animal ->
                Marker(
                    state = MarkerState(
                        LatLng(animal.location.latitude, animal.location.longitude),
                    ),
                    title = animal.name,
                    snippet = animal.locationDescription,
                )
            }
            val userLat = uiState.userLatitude
            val userLng = uiState.userLongitude
            if (userLat != null && userLng != null) {
                Circle(
                    center = LatLng(userLat, userLng),
                    radius = 5000.0,
                    strokeColor = colors.white,
                    fillColor = androidx.compose.ui.graphics.Color(0xABA20D08),
                )
            }
        }

        Button(
            onClick = { requestUserLocation() },
            enabled = !uiState.locationLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            if (uiState.locationLoading) {
                CircularProgressIndicator()
            } else {
                Text("Minha localização")
            }
        }
    }
}
