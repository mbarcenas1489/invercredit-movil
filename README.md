# Créditos Android

## Entorno de compilación

- JDK 17 para ejecutar Gradle.
- Gradle 8.7 (usar el wrapper incluido).
- Android Gradle Plugin 8.6.1.
- Kotlin 1.9.24.
- Android SDK Platform 35.

En Android Studio, abrir la carpeta raíz y seleccionar un JDK 17 en
**Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK**.
También se puede usar `GRADLE_LOCAL_JAVA_HOME` si `.gradle/config.properties`
contiene `java.home` apuntando a ese JDK. Este archivo es local y no se versiona.
El Java incluido con versiones recientes de Android Studio puede ser demasiado
nuevo para Gradle 8.7; Java 25 requiere Gradle 9.1 o posterior.

Configurar `sdk.dir` en `local.properties` con la ruta local del Android SDK y
ejecutar **Sync Project with Gradle Files**.

Para compilar desde PowerShell, configurar `JAVA_HOME` para esa sesión:

```powershell
$env:JAVA_HOME = 'C:\ruta\al\jdk-17'
.\gradlew.bat :app:assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Error de conexión local de Java en Windows

Si Gradle falla con `Unable to establish loopback connection` y la causa incluye
`UnixDomainSockets.connect` / `Invalid argument: connect`, probar en esa sesión:

```powershell
$env:JAVA_TOOL_OPTIONS = '-Djdk.net.unixdomain.tmpdir=C:/Windows/Temp'
.\gradlew.bat :app:assembleDebug
```

La carpeta debe existir y ser escribible. Para aplicar el ajuste solo al JDK
elegido también se puede establecer `jdk.net.unixdomain.tmpdir=C:/Windows/Temp`
en su archivo `conf/net.properties`.

Referencias: [compatibilidad de Gradle y Java](https://docs.gradle.org/current/userguide/compatibility.html)
y [Android Gradle Plugin 8.6](https://developer.android.com/build/releases/agp-8-6-0-release-notes).
