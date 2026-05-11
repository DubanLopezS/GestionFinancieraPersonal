# Identificación de Vulnerabilidades del Sistema
## Escenarios alternativos en las funcionalidades ya creadas

---

## 1. Registro de usuario

Al enviar un campo vacío, el sistema retorna un error 400 con el mensaje correcto, por ejemplo "El Username es obligatorio". Hasta ahí bien. El problema es que junto con ese mensaje también devuelve el stack trace completo, o sea le muestra a cualquiera el nombre de las clases internas, la versión de Spring que usa y cómo está organizado el código. Si alguien llama la API directo sin pasar por el frontend puede ver todo eso y usarlo para buscar vulnerabilidades conocidas en esas versiones.

Al mandar letras en el teléfono, el sistema sí valida que el campo tenga 10 dígitos numéricos y retorna un error 400 con ese mensaje. Esta validación sí está implementada correctamente. El problema, igual que en el caso anterior, es que junto con el mensaje retorna el stack trace completo.

Al mandar un nombre muy largo, el sistema retorna un error 400 con el mensaje "Nombre inválido". La validación sí está implementada. El problema sigue siendo el mismo: devuelve el stack trace completo junto con el mensaje de error.

Si la base de datos se cae, el sistema directamente no arranca. Hibernate intenta conectarse al iniciar y si la contraseña está mal o no hay conexión, el servidor nunca levanta y cualquier petición que llegue recibe un "Failed to fetch" sin ninguna explicación útil. No hay manejo de este escenario ni mensajes de error claros para el usuario.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** configurar un manejador global de excepciones (`@ControllerAdvice`) que devuelva solo el mensaje de error sin exponer el trace interno.

---

## 2. Login

Al mandar los campos vacíos, el sistema retorna un error 400 con el mensaje "Debe completar los campos requeridos". La validación sí está implementada. El problema es que, igual que en el registro, devuelve el stack trace completo en la respuesta.

Al mandar un correo con formato inválido, el sistema retorna un error 400 con el mensaje "El formato del correo no es válido". La validación está implementada y detecta correos mal escritos. El problema sigue siendo la exposición del stack trace.

Se probó intentar adivinar la contraseña ejecutando la petición 20 veces seguidas con contraseña incorrecta y el sistema respondió igual las 20 veces sin bloquear nada. No hay rate limiting ni control de intentos fallidos. Alguien podría escribir un script que pruebe miles de contraseñas automaticamente y el sistema nunca lo detendría.

Al mandar un correo existente con contraseña incorrecta, el sistema retorna "Credenciales inválidas" sin importar si el correo existe o no. Esto está bien implementado porque no le revela al atacante cuáles correos están registrados en el sistema.

**Nivel de riesgo general:** Alto (por la ausencia de rate limiting). **Recomendación para versión siguiente:** implementar bloqueo temporal después de N intentos fallidos y configurar el manejador global de excepciones.

---

## 3. Logout

Se probó el endpoint de logout ejecutándolo varias veces seguidas desde Swagger y siempre retorna un código 200 OK sin importar si existe o no una sesión activa. Incluso al llamarlo sin haber hecho login previamente, el sistema responde exactamente igual, lo que evidencia que no hay ningún tipo de validación ni manejo real de sesión. En la práctica, el endpoint no realiza ninguna acción, es solo un placeholder que simula cerrar sesión. Esto puede convertirse en un problema grave si en el futuro se implementa autenticación y no se maneja correctamente la invalidación de tokens.

**Nivel de riesgo:** Alto. **Recomendación para versión siguiente:** implementar tokens JWT y que el logout los invalide correctamente. Por ahora al menos documentar que está incompleto.

---

## 4. Crear cuenta

Al enviar un `idUsuario` que no existe en la base de datos, el sistema respondió con 400 y el mensaje "Usuario no encontrado", lo cual es el comportamiento correcto. Sin embargo, la respuesta incluye el stack trace completo, exponiendo rutas de clases, versiones de librerias y estructura interna del proyecto.

Al enviar el valor `"CRIPTO"` en el campo `tipo`, que no corresponde a ningún valor válido del enum (solo acepta `BANCARIA` o `EFECTIVO`), el sistema respondió con 400 y el mensaje "Tipo de cuenta inválido". Hay validación, pero la respuesta igualmente expone el stack trace completo.

Al enviar un saldo inicial de `999999999999999` con datos válidos, el sistema respondió con 200 y creó la cuenta sin ninguna restricción. El problema no es el monto en sí sino que el sistema usa `double` para representar dinero, lo que introduce errores de presición en operaciones posteriores con decimales. En un sistema financiero esto puede generar descuadres silenciosos en saldos y cálculos.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** reemplazar `double` por `BigDecimal` en todas las entidades y casos de uso que manejen valores monetarios. Agregar `@Transactional` al caso de uso.

---

## 5. Listar cuentas por usuario

Al enviar el UUID de un usuario diferente al autenticado, el sistema no verifica quién hace la petición, simplemente busca las cuentas del UUID recibido y las devuelve. Cualquier persona que conozca el UUID de otro usuario puede ver sus cuentas sin ningún control de acceso. Esta vulnerabilidad se conoce como IDOR (Insecure Direct Object Reference) y es bastante grave en un sistema financiero.

Al enviar un valor con formato incorrecto como `abc-123-xyz` en el campo `idUsuario`, Swagger bloqueó la petición antes de enviarla al servidor indicando que el valor debe ser un GUID válido. La validación existe a nivel de cliente pero no se puede confirmar si el servidor también la aplica por su cuenta.

**Nivel de riesgo general:** Alto (por el IDOR). **Recomendación para versión siguiente:** implementar JWT y verificar en el backend que el `idUsuario` del request coincida con el usuario autenticado en el token.

---

## 6. Crear categoría

Al enviar el campo `nombre` vacío, el sistema respondió con 400 y el mensaje "El nombre es obligatorio". Hay validación, pero la respuesta incluye el stack trace completo igual que en los endpoints anteriores.

Al enviar `"INVALIDO"` en el campo `tipo`, el sistema respondió con 400 y el mensaje "Tipo de transacción inválida". La validación del enum está implementada, pero la respuesta sigue exponiendo el stack trace.

Al enviar un nombre de más de 200 caracteres, el sistema respondió con 200 y creó la categoría sin ninguna restricción de longitud. No hay validación de tamaño en el campo `nombre`, lo que puede causar truncamiento silencioso en la base de datos si la columna tiene un límite definido, o consumo excesivo de almacenamiento si no lo tiene.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** agregar `@Size(max=100)` en el DTO de entrada e implementar el manejador global de excepciones.

---

## 7. Registrar transacción

Al enviar UUIDs que no existen en la base de datos, el sistema respondió con 400 y el mensaje "Usuario no encontrado", lo cual es el comportamiento correcto. La respuesta incluye el stack trace completo como en los demás endpoints.

Al enviar `"abc"` en el campo `monto`, Jackson falló antes de llegar al caso de uso con el mensaje "Cannot deserialize value of type `double` from String", lo que bloquea la petición. Sin embargo, el mensaje de error expone el tipo de dato interno (`double`) y la estructura del DTO, revelando detalles de implementación.

Al intentar registrar una transacción en la cuenta de otro usuario enviando un `idUsuario` inexistente, el sistema rechazó la petición con "Usuario no encontrado". Esto indica que valida la existencia del usuario antes de proceder. Sin embargo, esta validación depende del `idUsuario` que viene en el body del request, no de un token autenticado. Si alguien conoce el UUID real de otro usuario puede registrar transacciones en sus cuentas sin ningún control adicional.

En el flujo normal se confirmó que el sistema funciona correctamente, registra el gasto y actualiza el saldo. Sin embargo, el campo `monto` usa `double` internamente, lo que introduce errores de presición con decimales. Adicionalmente, no hay `@Transactional` explícito en el caso de uso, por lo que si el sistema falla después de actualizar el saldo pero antes de guardar la transacción, los datos pueden quedar inconsistentes.

**Nivel de riesgo general:** Alto. **Recomendación para versión siguiente:** usar `BigDecimal` en vez de `double`, agregar `@Transactional` al caso de uso e implementar JWT para extraer el `idUsuario` del token y no del body.

---

## 8. Actualizar transacción

Al enviar un `idTransaccion` que no existe en la base de datos, el sistema respondió con 400 y el mensaje "Transacción no encontrada". Hay validación, pero la respuesta expone el stack trace completo.

Al enviar el ID de una transacción real junto con un `idUsuario` que no existe, el sistema respondió con 400 y el mensaje "La transacción no pertenece al usuario", lo que indica que hay una verificación de propiedad. Sin embargo, esta validación depende del `idUsuario` que viene en el body del request y no de un token autenticado. Si alguien conoce el UUID real de otro usuario podría intentar modificar sus transacciones.

En el flujo normal el sistema actualizó la categoría de la transacción y devolvió los datos actualizados correctamente. No se encontraron problemas adicionales en este flujo.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** implementar JWT y extraer el `idUsuario` del token, no del body del request.

---

## 9. Transferencia entre cuentas

Al enviar la misma cuenta como origen y destino, el sistema respondió con 400 y el mensaje "Las cuentas deben ser diferentes". Esta validación está bien implementada.

Al enviar un monto mayor al saldo disponible, el sistema respondió con 400 y el mensaje "Saldo insuficiente". Esta validación también está bien implementada.

Al enviar `montoOrigen: 100` y `montoDestino: 200`, el sistema aceptó la operación con 200 y creó la transferencia sin ningún error. Esto significa que se pueden crear transferencias donde la cuenta destino recibe más dinero del que sale de la cuenta origen, generando dinero de la nada en la base de datos. No hay ninguna validación que verifique que ambos montos sean iguales.


**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:**  validar que `montoOrigen == montoDestino` antes de procesar la transferencia.

---

## 10. Resumen de gastos por categoría

Al consultar una cuenta que no tiene transacciones registradas, el sistema respondió con 200 y devolvió un array vacío `[]`. El caso de división por cero está manejado implícitamente: si no hay gastos, no hay cálculo. Este comportamiento es correcto.

Al enviar el `cuentaId` de una cuenta real junto con un `usuarioId` que no le pertenece, el sistema respondió con 400 y el mensaje "La cuenta no pertenece al usuario", lo que indica que hay una verificación de propiedad. Sin embargo, esta validación depende del `usuarioId` que viene en el query parameter y no de un token autenticado. Si alguien conoce el UUID real del dueño de la cuenta puede ver su resumen de gastos sin restricción.

En el flujo normal el sistema funciona y devuelve los porcentajes calculados correctamente. Sin embargo, el sistema usa `double` para los cálculos, lo que puede generar que los porcentajes no sumen exactamente 100% en ciertos casos con montos decimales.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** implementar JWT, extraer el `usuarioId` del token y reemplazar `double` por `BigDecimal` para los cálculos de porcentaje.

---

## 11. Listado de transacciones por categoría

Al enviar el `cuentaId` de una cuenta real junto con un `usuarioId` que no le pertenece, el sistema respondió con 400 y el mensaje "La cuenta no le pertenece al usuario". Hay verificación de propiedad, pero igual que en otros endpoints depende del `usuarioId` que viene en el query parameter y no de un token autenticado. Si alguien conoce el UUID real del dueño puede ver todas sus transacciones sin restricción.

El endpoint devuelve todas las transacciones de una cuenta en una sola respuesta sin ningún mecanismo de paginación. Esto no se puede demostrar con pocos registros, pero en una cuenta con miles de transacciones el sistema cargaría todo en memoria de una sola vez, lo que puede causar timeouts y consumo excesivo de recursos del servidor.

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** implementar paginación con `Pageable` en el repositorio, recibir parámetros `page` y `size` en el endpoint, e implementar JWT para la validación de acceso.

## 12. Configuración general

Consultas SQL visibles en los logs: El sistema está configurado para imprimir en la consola cada consulta que hace a la base de datos. Esto es útil mientras se está desarrollando porque ayuda a ver qué está pasando, pero si el sistema estuviera en producción cualquier persona con acceso a los logs podría ver cómo está estructurada la base de datos. 

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** desactivar esa opción antes de desplegar el sistema.

Mensajes que revelan qué usuarios existen: En la funcionalidad de registro, se probó intentar crear una cuenta con un correo que ya estaba registrado y luego con un username que ya existía. El sistema respondió con mensajes diferentes en cada caso. Eso hace posible que alguien pruebe correos o usernames uno por uno para descubrir cuáles están registrados en el sistema, lo que compromete la privacidad de los usuarios. 

**Nivel de riesgo general:** Medio. **Recomendación para versión siguiente:** usar un solo mensaje genérico para los dos casos, sin indicar cuál campo es el problema.