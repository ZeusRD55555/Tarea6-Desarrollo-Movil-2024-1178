package com.example.tarea6

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tarea6.ui.theme.Tarea6Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea6Theme {
                AplicacionPrincipal()
            }
        }
    }
}


suspend fun obtenerJson(urlDestino: String): String = withContext(Dispatchers.IO) {
    try {
        val url = URL(urlDestino)
        val conexion = url.openConnection() as HttpURLConnection
        conexion.requestMethod = "GET"
        conexion.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
        conexion.connectTimeout = 8000
        conexion.readTimeout = 8000
        
        val codigoRespuesta = conexion.responseCode
        if (codigoRespuesta in 200..299) {
            BufferedReader(InputStreamReader(conexion.inputStream)).use { lector ->
                lector.readText()
            }
        } else {
            ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AplicacionPrincipal() {
    var pantallaActual by remember { mutableStateOf("inicio") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when (pantallaActual) {
                            "inicio" -> "Caja de Herramientas (Menú)"
                            "genero" -> "Predictor de Género"
                            "persona_aleatoria" -> "Persona Aleatoria"
                            "universidades" -> "Universidades por País"
                            "clima" -> "Clima en RD"
                            "pokemon" -> "Buscador Pokémon"
                            "wordpress" -> "Noticias WordPress"
                            "acerca_de" -> "Acerca de"
                            else -> "Aplicación"
                        }
                    ) 
                },
                navigationIcon = {
                    if (pantallaActual != "inicio") {
                        IconButton(onClick = { pantallaActual = "inicio" }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { espacioRelleno ->
        Box(modifier = Modifier.padding(espacioRelleno)) {
            when (pantallaActual) {
                "inicio" -> PantallaInicio(alNavegar = { pantallaActual = it })
                "genero" -> PantallaGenero()
                "persona_aleatoria" -> PantallaPersonaAleatoria()
                "universidades" -> PantallaUniversidades()
                "clima" -> PantallaClima()
                "pokemon" -> PantallaPokemon()
                "wordpress" -> PantallaWordPress()
                "acerca_de" -> PantallaAcercaDe()
            }
        }
    }
}


// 1. PANTALLA PRINCIPAL / CAJA DE HERRAMIENTAS
// ==========================================
@Composable
fun PantallaInicio(alNavegar: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.caja_herramientas),
            contentDescription = "Caja de herramientas",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenido a la Caja de Herramientas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Selecciona una herramienta",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val herramientas = listOf(
            Triple("genero", "Predecir Género", Icons.Default.Wc),
            Triple("persona_aleatoria", "Persona Aleatoria", Icons.Default.Person),
            Triple("universidades", "Universidades", Icons.Default.School),
            Triple("clima", "Clima en RD", Icons.Default.WbSunny),
            Triple("pokemon", "Pokémon", Icons.Default.CatchingPokemon),
            Triple("wordpress", "Noticias WordPress", Icons.AutoMirrored.Filled.Article),
            Triple("acerca_de", "Acerca de", Icons.Default.Info)
        )

        herramientas.forEach { (ruta, titulo, icono) ->
            ElevatedCard(
                onClick = { alNavegar(ruta) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icono, contentDescription = titulo, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = titulo, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ======================
// 2. PREDICTOR DE GÉNERO
// ======================
@Composable
fun PantallaGenero() {
    var entradaNombre by remember { mutableStateOf("") }
    var resultadoGenero by remember { mutableStateOf<String?>(null) }
    var resultadoProbabilidad by remember { mutableStateOf<Double?>(null) }
    var cargando by remember { mutableStateOf(false) }
    val ambitoCorrutina = rememberCoroutineScope()

    val colorFondo = when (resultadoGenero?.lowercase()) {
        "male" -> Color(0xFFE3F2FD)
        "female" -> Color(0xFFFCE4EC)
        else -> MaterialTheme.colorScheme.background
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = entradaNombre,
            onValueChange = { entradaNombre = it },
            label = { Text("Ingrese un nombre (ej. irma, juan)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (entradaNombre.isNotBlank()) {
                    cargando = true
                    ambitoCorrutina.launch {
                        val textoJson = obtenerJson("https://api.genderize.io/?name=${entradaNombre.trim()}")
                        if (textoJson.isNotBlank()) {
                            val objetoJson = JSONObject(textoJson)
                            resultadoGenero = objetoJson.optString("gender", "desconocido")
                            resultadoProbabilidad = objetoJson.optDouble("probability", 0.0)
                        } else {
                            resultadoGenero = "error"
                        }
                        cargando = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Predecir Género")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (resultadoGenero != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (resultadoGenero?.lowercase()) {
                        "male" -> Color(0xFFBBDEFB)
                        "female" -> Color(0xFFF8BBD0)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Nombre: ${entradaNombre.replaceFirstChar { it.uppercase() }}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Género: ${if (resultadoGenero == "male") "Masculino 💙" else if (resultadoGenero == "female") "Femenino 💖" else "No encontrado"}",
                        fontSize = 18.sp
                    )
                    if (resultadoProbabilidad != null) {
                        Text(text = "Probabilidad: ${(resultadoProbabilidad!! * 100)}%", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// =====================
// 3. PERSONA ALEATORIA
// =====================
@Composable
fun PantallaPersonaAleatoria() {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var urlFoto by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(0) }
    var cargando by remember { mutableStateOf(true) }
    val ambitoCorrutina = rememberCoroutineScope()

    fun cargarUsuario() {
        cargando = true
        ambitoCorrutina.launch {
            val textoJson = obtenerJson("https://randomuser.me/api/?results=1")
            if (textoJson.isNotBlank()) {
                val raiz = JSONObject(textoJson)
                val resultados = raiz.getJSONArray("results")
                val usuario = resultados.getJSONObject(0)
                val objetoNombre = usuario.getJSONObject("name")
                nombre = objetoNombre.getString("first")
                apellido = objetoNombre.getString("last")
                urlFoto = usuario.getJSONObject("picture").getString("large")
                val objetoNacimiento = usuario.getJSONObject("dob")
                fechaNacimiento = objetoNacimiento.getString("date").take(10)
                edad = objetoNacimiento.getInt("age")
            }
            cargando = false
        }
    }

    LaunchedEffect(Unit) {
        cargarUsuario()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = urlFoto,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "$nombre $apellido", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha de Nacimiento: $fechaNacimiento", fontSize = 16.sp)
                    Text(text = "Edad: $edad años", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { cargarUsuario() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cargar otra persona")
            }
        }
    }
}

// ==========================================
// 4. UNIVERSIDADES POR PAÍS (Universities API)
// ==========================================
data class Universidad(val nombre: String, val dominio: String, val paginaWeb: String)

@Composable
fun PantallaUniversidades() {
    var entradaPais by remember { mutableStateOf("") }
    var listaUniversidades by remember { mutableStateOf<List<Universidad>>(emptyList()) }
    var cargando by remember { mutableStateOf(false) }
    val ambitoCorrutina = rememberCoroutineScope()
    val manejadorUri = LocalUriHandler.current

    fun buscarUniversidades() {
        if (entradaPais.isNotBlank()) {
            cargando = true
            ambitoCorrutina.launch {
                val paisFormateado = entradaPais.trim().replace(" ", "+")
                val textoJson = obtenerJson("https://adamix.net/proxy.php?country=$paisFormateado")
                val listaTemporal = mutableListOf<Universidad>()
                if (textoJson.isNotBlank()) {
                    try {
                        val arregloJson = JSONArray(textoJson)
                        for (i in 0 until arregloJson.length()) {
                            val objeto = arregloJson.getJSONObject(i)
                            val nombreUni = objeto.optString("name", "Sin nombre")
                            val dominiosArr = objeto.optJSONArray("domains")
                            val dominio = if (dominiosArr != null && dominiosArr.length() > 0) dominiosArr.getString(0) else ""
                            val paginasArr = objeto.optJSONArray("web_pages")
                            val paginaWeb = if (paginasArr != null && paginasArr.length() > 0) paginasArr.getString(0) else ""
                            listaTemporal.add(Universidad(nombreUni, dominio, paginaWeb))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                listaUniversidades = listaTemporal
                cargando = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = entradaPais,
            onValueChange = { entradaPais = it },
            label = { Text("País en inglés (ej. Dominican Republic)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { buscarUniversidades() }, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar Universidades")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(listaUniversidades) { uni ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = uni.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            if (uni.dominio.isNotBlank()) {
                                Text(text = "Dominio: ${uni.dominio}", fontSize = 14.sp, color = Color.Gray)
                            }
                            if (uni.paginaWeb.isNotBlank()) {
                                Text(
                                    text = "Visitar web: ${uni.paginaWeb}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .clickable {
                                            try {
                                                manejadorUri.openUri(uni.paginaWeb)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. CLIMA EN RD (Open-Meteo API)
// ==========================================
@Composable
fun PantallaClima() {
    var temperatura by remember { mutableStateOf("") }
    var humedad by remember { mutableStateOf("") }
    var velocidadViento by remember { mutableStateOf("") }
    var descripcionClima by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    val ambitoCorrutina = rememberCoroutineScope()

    val fechaActual = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es")).format(Date())

    LaunchedEffect(Unit) {
        ambitoCorrutina.launch {
            val textoJson = obtenerJson("https://api.open-meteo.com/v1/forecast?latitude=18.5001&longitude=-69.9886&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m")
            if (textoJson.isNotBlank()) {
                val objeto = JSONObject(textoJson)
                val actual = objeto.getJSONObject("current")
                temperatura = "${actual.getDouble("temperature_2m")} °C"
                humedad = "${actual.getDouble("relative_humidity_2m")}%"
                velocidadViento = "${actual.getDouble("wind_speed_10m")} km/h"
                val codigo = actual.getInt("weather_code")
                descripcionClima = obtenerDescripcionClima(codigo)
            }
            cargando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.WbSunny, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFFFFC107))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Santo Domingo, República Dominicana", fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text(text = fechaActual.replaceFirstChar { it.uppercase() }, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDetalleClima("Estado del clima:", descripcionClima)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleClima("Temperatura:", temperatura)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleClima("Humedad:", humedad)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleClima("Viento:", velocidadViento)
                }
            }
        }
    }
}

@Composable
fun FilaDetalleClima(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, fontWeight = FontWeight.Medium)
        Text(text = valor, fontWeight = FontWeight.Bold)
    }
}

fun obtenerDescripcionClima(codigo: Int): String {
    return when (codigo) {
        0 -> "Despejado ☀️"
        1, 2, 3 -> "Parcialmente nublado ⛅"
        45, 48 -> "Neblina 🌫️"
        51, 53, 55, 56, 57 -> "Llovizna 🌧️"
        61, 63, 65, 66, 67 -> "Lluvia 🌧️"
        71, 73, 75, 77 -> "Nieve ❄️"
        80, 81, 82 -> "Chubascos 🌦️"
        95, 96, 99 -> "Tormenta eléctrica ⚡"
        else -> "Nublado / Variable ☁️"
    }
}

// ==========================================
// 6. POKÉMON (PokeAPI)
// ==========================================
@Composable
fun PantallaPokemon() {
    var entradaPokemon by remember { mutableStateOf("") }
    var nombrePokemon by remember { mutableStateOf("") }
    var urlImagenPokemon by remember { mutableStateOf("") }
    var experienciaBase by remember { mutableIntStateOf(0) }
    var habilidades by remember { mutableStateOf<List<String>>(emptyList()) }
    var urlAudio by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val ambitoCorrutina = rememberCoroutineScope()

    fun buscarPokemon() {
        if (entradaPokemon.isNotBlank()) {
            cargando = true
            mensajeError = null
            ambitoCorrutina.launch {
                val consulta = entradaPokemon.trim().lowercase()
                val textoJson = obtenerJson("https://pokeapi.co/api/v2/pokemon/$consulta")
                if (textoJson.isNotBlank()) {
                    try {
                        val objeto = JSONObject(textoJson)
                        nombrePokemon = objeto.getString("name")
                        experienciaBase = objeto.getInt("base_experience")
                        urlImagenPokemon = objeto.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").optString("front_default")
                        if (urlImagenPokemon.isBlank()) {
                            urlImagenPokemon = objeto.getJSONObject("sprites").getString("front_default")
                        }

                        val arregloHabilidades = objeto.getJSONArray("abilities")
                        val listaHabilidades = mutableListOf<String>()
                        for (i in 0 until arregloHabilidades.length()) {
                            val nombreHabilidad = arregloHabilidades.getJSONObject(i).getJSONObject("ability").getString("name")
                            listaHabilidades.add(nombreHabilidad)
                        }
                        habilidades = listaHabilidades

                        val objetoGritos = objeto.optJSONObject("cries")
                        urlAudio = objetoGritos?.optString("latest")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        mensajeError = "Pokémon no encontrado"
                    }
                } else {
                    mensajeError = "Error al conectar o Pokémon no existe"
                }
                cargando = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = entradaPokemon,
            onValueChange = { entradaPokemon = it },
            label = { Text("Nombre del Pokémon (ej. pikachu, charizard)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { buscarPokemon() }, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (mensajeError != null) {
            Text(text = mensajeError!!, color = Color.Red)
        } else if (nombrePokemon.isNotBlank()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = urlImagenPokemon,
                        contentDescription = nombrePokemon,
                        modifier = Modifier.size(160.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = nombrePokemon.replaceFirstChar { it.uppercase() }, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Experiencia Base: $experienciaBase", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Habilidades:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    habilidades.forEach { habilidad ->
                        Text(text = "• $habilidad", fontSize = 14.sp)
                    }

                    if (!urlAudio.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            try {
                                MediaPlayer().apply {
                                    setDataSource(urlAudio)
                                    prepareAsync()
                                    setOnPreparedListener { start() }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reproducir Sonido")
                        }
                    }
                }
            }
        }
    }
}

// =====================
// 7. NOTICIAS WORDPRESS
// =====================
data class Noticia(val titulo: String, val resumen: String, val enlace: String)

@Composable
fun PantallaWordPress() {
    var publicaciones by remember { mutableStateOf<List<Noticia>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    val ambitoCorrutina = rememberCoroutineScope()
    val manejadorUri = LocalUriHandler.current

    val nombreSitio = "Disfrutando del Camino"

    LaunchedEffect(Unit) {
        ambitoCorrutina.launch {
            val textoJson = obtenerJson("https://disfrutandodelcamino.com/wp-json/wp/v2/posts?per_page=3")
            val listaTemporal = mutableListOf<Noticia>()
            if (textoJson.isNotBlank()) {
                try {
                    val arregloJson = JSONArray(textoJson)
                    for (i in 0 until arregloJson.length()) {
                        val objeto = arregloJson.getJSONObject(i)
                        val titulo = objeto.getJSONObject("title").getString("rendered")
                            .replace("&#8217;", "'")
                            .replace("&#8220;", "\"")
                            .replace("&#8221;", "\"")
                            .replace("&#8211;", "-")
                        val resumenHtml = objeto.getJSONObject("excerpt").getString("rendered")
                            .replace(Regex("<.*?>"), "")
                            .replace("&#8217;", "'")
                            .replace("&#8220;", "\"")
                            .replace("&#8221;", "\"")
                            .replace("&#8211;", "-")
                            .trim()
                        val enlace = objeto.getString("link")
                        listaTemporal.add(Noticia(titulo, resumenHtml, enlace))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            publicaciones = listaTemporal
            cargando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_disfrutando_del_camino),
            contentDescription = "Logo Disfrutando del Camino",
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = nombreSitio, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Últimas 3 noticias publicadas", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else {
            publicaciones.forEach { publicacion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = publicacion.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = publicacion.resumen, fontSize = 14.sp, maxLines = 3)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Visitar noticia",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                try {
                                    manejadorUri.openUri(publicacion.enlace)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// ============
// 8. ACERCA DE
// ============
@Composable
fun PantallaAcercaDe() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto_para_el_cv),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Angel Olaverria", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = "Estudiante de Desarrollo de Software", fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Contacto", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                FilaContacto(icono = Icons.Default.Email, texto = "angelj2992@gmail.com")
                FilaContacto(icono = Icons.Default.Phone, texto = "+1 (809) 317-9166")
                FilaContacto(icono = Icons.Default.LocationOn, texto = "Santo Domingo Este, Rep. Dom.")
            }
        }
    }
}

@Composable
fun FilaContacto(icono: ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = texto, fontSize = 15.sp)
    }
}
