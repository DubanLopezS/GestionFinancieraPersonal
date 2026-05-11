# Documentación Técnica
## Sistema de Gestión Financiera Personal

**Universidad de Antioquia**
**Ingeniería de Sistemas**
**Fábrica Escuela 2026-1**

---

## ¿De qué trata el proyecto?

El sistema es una API REST para manejar finanzas personales. La idea es que una persona pueda registrar sus ingresos y gastos, ver cuánto tiene en cada cuenta y consultar reportes de en qué se está yendo la plata.

Se puede crear usuarios, agregar cuentas bancarias o de efectivo, registrar movimientos, hacer transferencias entre cuentas y organizar todo por categorías. La base de datos está en PostgreSQL usando Supabase que es un servicio en la nube, y la API se construyó con Java 21 y Spring Boot.

---

## Tecnologías que se usaron

| Tecnología | Para qué se usa |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 3.5.12 | Framework que facilita crear APIs REST |
| Spring Data JPA | Para conectarse a la base de datos sin escribir SQL a mano |
| Spring Security Crypto | Solo para cifrar contraseñas con BCrypt |
| Lombok | Para no escribir tanto código repetitivo (getters, constructores, etc) |
| PostgreSQL en Supabase | Base de datos en la nube |
| SpringDoc / Swagger | Genera la documentación de la API automaticamente |

---

## Estructura del proyecto

El proyecto sigue una arquitectura en capas. Cada capa tiene una responsabilidad clara y no deberia meterse en los asuntos de las otras.

```
src/main/java/com/fabrica/gestionfinancierapersonal/
│
├── domain/               <- Las clases del negocio (lo más importante)
│   ├── model/            <- Usuario, Cuenta, Categoria, Transaccion
│   └── enums/            <- Valores fijos: TipoCuenta, Moneda, etc.
│
├── application/          <- La lógica de cada funcionalidad
│   ├── dtos/             <- Objetos que definen qué datos entran y salen
│   ├── repository/       <- Interfaces que dicen qué operaciones hay con la BD
│   └── usecases/         <- Un archivo por funcionalidad (RegistrarUsuario, etc.)
│
├── interfaces/
│   └── controllers/      <- Los endpoints que reciben las peticiones HTTP
│
└── persistance/
    └── repository/       <- La implementación real de JPA
```

La separación funciona así: el controlador recibe la petición, llama al caso de uso, el caso de uso usa el repositorio para leer o guardar en la base de datos, y devuelve la respuesta.

---

## Las entidades principales

Son las cuatro clases que representan los datos del sistema. JPA las mapea a tablas en PostgreSQL.

### Usuario
Guarda los datos de una persona registrada: username, nombre, apellido, correo, contraseña (cifrada con BCrypt) y teléfono. Cada usuario tiene una lista de cuentas. El ID se genera automaticamente como UUID y el rol siempre arranca como USUARIO.

### Cuenta
Representa una cuenta de dinero. Puede ser BANCARIA (tiene nombre) o EFECTIVO (siempre se llama "Efectivo"). Guarda el saldo actual y la lista de transacciones. Cuando se agrega un gasto, la cuenta valida que haya saldo suficiente antes de restar.

### Categoria
Sirve para clasificar las transacciones. Puede ser de tipo INGRESO o GASTO. Hay categorias de sistema que ven todos los usuarios (como "Transferencia_Salida") y categorias personales que crea cada usuario.

### Transaccion
Representa un movimiento de dinero. Guarda el monto, el tipo (INGRESO o GASTO), la periodicidad, la fecha y la categoría. Si la transaccion es parte de una transferencia entre cuentas, tiene un `transferenciaId` que comparte con la transacción de la otra cuenta para poder relacionarlas.

---

## Los enums (valores fijos del sistema)

| Enum | Valores |
|---|---|
| TipoCuenta | BANCARIA, EFECTIVO |
| TipoTransaccion | INGRESO, GASTO |
| Moneda | COP, USD |
| Periodicidad | OCASIONAL, DIARIA, SEMANAL, MENSUAL, ANUAL |
| Rol | USUARIO, ADMIN |

---

## Los endpoints disponibles

### Usuarios — `/api/usuarios`

| Método | Ruta | Qué hace |
|---|---|---|
| POST | `/signup` | Registra un usuario nuevo |
| POST | `/login` | Inicia sesión, retorna el idUsuario y el nombre |
| POST | `/logout` | Cierra sesión (pendiente de implementar) |

### Cuentas — `/api/cuentas`

| Método | Ruta | Qué hace |
|---|---|---|
| POST | `/crear` | Crea una cuenta nueva |
| GET | `/listar?idUsuario=` | Lista las cuentas de un usuario |

### Categorias — `/api/categorias`

| Método | Ruta | Qué hace |
|---|---|---|
| POST | `/crear` | Crea una categoria nueva |
| GET | `/listar?idUsuario=&tipo=` | Lista categorias por tipo |

### Transacciones — `/api/transaccion`

| Método | Ruta | Qué hace |
|---|---|---|
| POST | `/registrar` | Registra un ingreso o gasto |
| PUT | `/actualizar` | Cambia la categoría de una transacción |
| POST | `/transferencias` | Transfiere dinero entre dos cuentas |

### Reportes — `/api/reportes`

| Método | Ruta | Qué hace |
|---|---|---|
| GET | `/resumenCategorias?cuentaId=&usuarioId=` | Resumen de gastos por categoria con porcentajes |
| GET | `/transaccionesCategoria?cuentaId=&categoriaId=&usuarioId=` | Lista transacciones filtradas por cuenta y categoria |

---

## Cómo fluye una petición

Para entender cómo funciona el sistema por dentro, acá está el flujo de registrar una transacción como ejemplo:

```
1. Cliente manda POST /api/transaccion/registrar con el JSON

2. TransaccionController recibe la petición
   └── deserializa el JSON a RegistrarTransaccionRequest

3. RegistrarTransaccion (caso de uso) ejecuta la lógica:
   ├── busca el usuario en la BD
   ├── busca la cuenta y verifica que sea del usuario
   ├── busca la categoria y verifica que coincida con el tipo
   ├── crea la Transaccion (valida monto > 0, tipo, periodicidad)
   ├── llama a cuenta.agregarTransaccion() -> actualiza el saldo
   ├── guarda la transaccion en la BD
   └── actualiza la cuenta en la BD

4. Retorna RegistrarTransaccionResponse con el saldo actualizado

5. El cliente recibe { cuentaId, saldoActual }
```

### Flujo de una transferencia

La transferencia es el caso más complejo porque toca dos cuentas al mismo tiempo:

```
1. Se verifica que las dos cuentas sean del mismo usuario y distintas
2. Se verifica que haya saldo suficiente en la cuenta origen
3. Se genera un transferenciaId (UUID único)
4. Se crea un GASTO en la cuenta origen con ese transferenciaId
5. Se crea un INGRESO en la cuenta destino con el mismo transferenciaId
6. Se actualizan los saldos de las dos cuentas
7. Se retorna el transferenciaId y los IDs de las cuentas
```

El `transferenciaId` compartido es lo que permite saber que esas dos transacciones son partes de la misma transferencia.

---

## La base de datos

### Tablas

**usuarios**

| Columna | Tipo | Descripción |
|---|---|---|
| id_usuario | UUID | Clave primaria |
| username | VARCHAR | Unico en el sistema |
| nombre | VARCHAR | Nombre de pila |
| apellido | VARCHAR | Apellido |
| correo | VARCHAR | Correo electronico |
| contrasena | VARCHAR | Hash BCrypt, nunca en texto plano |
| telefono | VARCHAR | Numero de telefono |
| rol | VARCHAR | USUARIO o ADMIN |

**cuentas**

| Columna | Tipo | Descripción |
|---|---|---|
| id_cuenta | UUID | Clave primaria |
| usuario_id | UUID | FK a usuarios |
| nombre | VARCHAR | Nombre de la cuenta |
| saldo | DOUBLE PRECISION | Saldo actual |
| tipo | VARCHAR | BANCARIA o EFECTIVO |
| moneda | VARCHAR | COP o USD |

**categorias**

| Columna | Tipo | Descripción |
|---|---|---|
| id_categoria | UUID | Clave primaria |
| usuario_id | UUID | FK a usuarios (null si es de sistema) |
| nombre | VARCHAR | Nombre de la categoria |
| tipo | VARCHAR | INGRESO o GASTO |
| es_sistema | BOOLEAN | Si es true la ven todos los usuarios |

**transacciones**

| Columna | Tipo | Descripción |
|---|---|---|
| id_transaccion | UUID | Clave primaria |
| cuenta_id | UUID | FK a cuentas |
| categoria_id | UUID | FK a categorias |
| monto | DOUBLE PRECISION | Valor del movimiento |
| tipo | VARCHAR | INGRESO o GASTO |
| periodicidad | VARCHAR | OCASIONAL, DIARIA, etc. |
| fecha | TIMESTAMP | Cuándo se registró |
| transferencia_id | UUID | Nullable, une las dos partes de una transferencia |

### Relaciones entre tablas

Un usuario puede tener muchas cuentas. Cada cuenta puede tener muchas transacciones. Cada transacción pertenece a una categoría. Las categorías pueden ser del usuario o del sistema.

---

## Ejemplos de uso

### Registrar un usuario

```
POST /api/usuarios/signup

{
  "username": "juanf",
  "nombre": "Juan",
  "apellido": "Florez",
  "correo": "juan@correo.com",
  "contrasena": "MiClave123!",
  "telefono": "3001234567"
}
```

La contraseña debe tener al menos una mayuscula, un numero y un caracter especial. El sistema la cifra con BCrypt antes de guardarla.

Respuesta:
```json
{
  "idUsuario": "a1b2c3d4-...",
  "username": "juanf",
  "nombre": "Juan",
  "apellido": "Florez",
  "correo": "juan@correo.com"
}
```

### Hacer login

```
POST /api/usuarios/login

{
  "correo": "juan@correo.com",
  "contrasena": "MiClave123!"
}
```

Respuesta:
```json
{
  "idUsuario": "a1b2c3d4-...",
  "nombre": "Juan"
}
```

El sistema retorna solo el `idUsuario` y el nombre. Ese `idUsuario` hay que usarlo en todas las demas peticiones.

### Crear una cuenta bancaria

```
POST /api/cuentas/crear

{
  "idUsuario": "a1b2c3d4-...",
  "nombre": "Cuenta Bancolombia",
  "tipo": "BANCARIA",
  "saldoInicial": 1500000,
  "moneda": "COP"
}
```

Los tipos válidos son BANCARIA y EFECTIVO. Las monedas válidas son COP y USD.

### Registrar un gasto

```
POST /api/transaccion/registrar

{
  "idUsuario": "a1b2c3d4-...",
  "idCuenta": "b2c3d4e5-...",
  "monto": 85000,
  "tipoTransaccion": "GASTO",
  "periodicidad": "OCASIONAL",
  "idCategoria": "d4e5f6a1-..."
}
```

Respuesta:
```json
{
  "cuentaId": "b2c3d4e5-...",
  "saldoActual": 1415000.0
}
```

### Transferencia entre cuentas

```
POST /api/transaccion/transferencias

{
  "idCuentaOrigen": "b2c3d4e5-...",
  "idCuentaDestino": "c3d4e5f6-...",
  "idUsuario": "a1b2c3d4-...",
  "montoOrigen": 200000,
  "montoDestino": 200000
}
```

### Ver resumen de gastos

```
GET /api/reportes/resumenCategorias?cuentaId=b2c3d4e5-...&usuarioId=a1b2c3d4-...
```

Respuesta:
```json
[
  { "nombreCategoria": "Comida", "total": 350000.0, "porcentaje": 58.3 },
  { "nombreCategoria": "Transporte", "total": 250000.0, "porcentaje": 41.7 }
]
```

---

## Cómo correr el proyecto

Se necesita Java 21, Maven y conexion a internet para conectarse a Supabase.

```bash
# En Linux o Mac
./mvnw spring-boot:run

# En Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.datasource.password=TU_CONTRASEÑA"
```

Cuando aparezca `Started GestionfinancierapersonalApplication in X seconds` ya está listo. La API queda en `http://localhost:8080` y el Swagger en `http://localhost:8080/swagger-ui/index.html`.



