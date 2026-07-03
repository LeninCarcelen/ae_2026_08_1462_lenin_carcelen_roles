# events — Microservicio con autenticación AWS Cognito

Backend de una plataforma de reserva de entradas para eventos. Arquitectura por capas
idéntica al proyecto de referencia `students`, con seguridad Cognito (OAuth2 Resource
Server) igual que `demo_auth`.

Paquete base: `com.pucetec.events`

## Cómo correr el proyecto

```bash
./gradlew bootRun
```

La app levanta en `http://localhost:8080`, con base de datos H2 en memoria (se recrea en
cada arranque).

## Endpoints

| Método | Ruta | Acceso |
|---|---|---|
| GET | /api/events | Público |
| GET | /api/events/{id} | Público |
| POST | /api/events | Privado |
| POST | /api/attendees | Privado |
| POST | /api/reservations | Privado |
| PUT | /api/reservations/{id}/cancel | Privado |
| GET | /api/reservations | Privado |

## Obtener el access_token (pool de prueba de Cognito)

1. Abre en el navegador:
   ```
   https://us-east-1yzwnali2a.auth.us-east-1.amazoncognito.com/login?client_id=3gv2oqe4niko3s47srn1kitsk6&response_type=code&scope=email+openid+phone&redirect_uri=https%3A%2F%2Fd84l1y8p4kdic.cloudfront.net
   ```
2. Autorregístrate (Sign up) en la Hosted UI.
3. Copia el `code` de la URL de redirección (`...cloudfront.net/?code=XXXX`). Es de un solo
   uso y dura pocos minutos.
4. Canjéalo por el token:
   ```bash
   curl --location 'https://us-east-1yzwnali2a.auth.us-east-1.amazoncognito.com/oauth2/token' \
     --header 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode 'grant_type=authorization_code' \
     --data-urlencode 'client_id=3gv2oqe4niko3s47srn1kitsk6' \
     --data-urlencode 'client_secret=14qdd388f1j6fge52el3l5r2ouvcg5sperlno3701t2jj1chgeiu' \
     --data-urlencode 'code=<PEGA_TU_CODE_AQUI>' \
     --data-urlencode 'redirect_uri=https://d84l1y8p4kdic.cloudfront.net'
   ```
5. Usa el `access_token` de la respuesta en el header `Authorization: Bearer <access_token>`
   (o en la variable `access_token` de la colección de Postman incluida en `postman/`).

## Tests y cobertura

Los tests de la capa `services` están en
`src/test/kotlin/com/pucetec/events/services/`. Para ver el reporte de cobertura en
IntelliJ: clic derecho sobre la carpeta `services` (test) → **Run 'Tests in services' with
Coverage**.

## Evidencias a capturar (carpeta `evidencias/`)

1. `GET /api/events` respondiendo 200 sin token.
2. `POST /api/reservations` sin token respondiendo 401 Unauthorized.
3. `POST /api/reservations` con token válido respondiendo 200/201.
4. Login/registro en la Hosted UI y/o la respuesta del `/oauth2/token` con el `access_token`.
5. Reporte de cobertura de IntelliJ: `services` en Line 100% / Branch 100%.

## Antes de entregar

- [ ] Renombra el repo a `ae_puce_2026_[nrc]_[nombre]_[apellido]` y súbelo a GitHub.
- [ ] Verifica que las 5 capturas estén en `evidencias/`.
- [ ] Verifica que `services` tenga 100% líneas / 100% ramas.
- [ ] Pega el link del repositorio en la entrega.
