# Gastify

Aplicación Android para administrar finanzas personales mediante el registro de ingresos, gastos y un presupuesto mensual. La información se almacena localmente en el dispositivo mediante SQLite y Room.

## Funcionalidades

- Bienvenida personalizada y cambio de nombre del usuario.
- Registro de gastos con monto, descripción, categoría y fecha automática.
- Registro de ingresos con monto, descripción, fuente y fecha automática.
- Historial de movimientos.
- Resumen mensual de ingresos, gastos y saldo.
- Configuración del presupuesto mensual con alertas al acercarse o superar el límite.
- Validación de campos obligatorios y montos.
- Sección de inspiración dedicada a San Carlo Acutis.

## Tecnologías

- **Kotlin:** lenguaje de programación.
- **Jetpack Compose y Material 3:** interfaz de usuario.
- **Room y SQLite:** almacenamiento local.
- **ViewModel y Kotlin Coroutines:** administración del estado y operaciones asíncronas.
- **Gradle Kotlin DSL:** configuración y dependencias.

## Organización del código

| Componente | Responsabilidad |
|---|---|
| `modelo` | Representa los datos de ingresos, gastos y movimientos. |
| `datos` | Contiene entidades, DAO, repositorios y configuración de la base de datos. |
| `logica` | Implementa validaciones y cálculos financieros. |
| `viewmodel` | Coordina las operaciones y mantiene el estado de las pantallas. |
| Pantallas Compose | Presentan la información y reciben las acciones del usuario. |
| `ui/theme` | Define el tema visual de la aplicación. |

Los montos se almacenan como centavos enteros para evitar errores de precisión monetaria.

## Ejecución

1. Clonar el repositorio.
2. Abrir la carpeta del proyecto en Android Studio.
3. Sincronizar las dependencias de Gradle.
4. Seleccionar un dispositivo físico o emulador con Android 7.0 (API 24) o superior.
5. Ejecutar el módulo `app`.

La descarga inicial de herramientas y dependencias requiere conexión a internet. Los registros financieros se guardan en el dispositivo y no requieren un servidor externo.

## Contexto académico

Proyecto desarrollado para el curso de Programación II de Ingeniería en Sistemas de la Universidad Mariano Gálvez de Guatemala, Campus Jutiapa.

## Estado

Proyecto académico en desarrollo.