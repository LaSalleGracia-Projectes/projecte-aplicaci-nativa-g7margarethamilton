# 📱 Projecte **Aplicació Nativa – G7 Margaret Hamilton**

Aplicació **Android nativa** escrita en **Kotlin 2.0** amb **Jetpack Compose** que et permet:

* Gestionar **calendaris, horaris i tasques** (CRUD complet)
* **(Pròximament) Llista de la compra i mòdul d’exercicis personals** – ara mateix només hi ha una pantalla "Pròximament".
* Autenticar‑te amb **correu electrònic / contrasenya**. La integració amb **Google Sign‑In** està **limitada a 2 dispositius mòbils** i només funciona des del portàtil del desenvolupador durant les proves; per activar‑la en més dispositius caldria passar al pla de pagament de Google.
* Veure i editar el teu **perfil**, preferències i configuració
* Activar **tema fosc/clar** i canviar l’idioma. **(Pròximament) notificacions push**
* Accedir a seccions de **Contacta’ns** i **Sobre nosaltres**

---

## 🗂️ Estructura del repositori

```
projecte-aplicaci-nativa-g7margarethamilton/
 ├─ app/                      <- Mòdul Android principal
 │   ├─ src/main/java/com/…   <- Codi font (MVVM + Compose)
 │   ├─ src/main/res/         <- Recursos XML, icones, traduccions
 │   └─ build.gradle.kts
 ├─ gradle/                   <- Versió de plugins/dependències (libs.versions.toml)
 ├─ build.gradle.kts          <- Escriptura Kotlin DSL
 ├─ settings.gradle.kts
 └─ gradlew*                  <- Wrappers de Gradle
```

---

## 🚀 Posada en marxa

1. **Prerequisits**
   * **Android Studio Hedgehog** (o superior) amb
     * **Android Gradle Plugin 8.7.2**
     * **Kotlin 2.0** (ja inclòs al projecte)
   * **JDK 17**
   * Dispositiu o emulador amb **API 26+**

2. **Clona** el repositori i obre’l amb Android Studio:

   ```bash
   git clone https://github.com/…/projecte-aplicaci-nativa-g7margarethamilton.git
   ```

3. **(Opcional)** Per a l’inici de sessió amb Google, crea un **OAuth Client ID** a la Google Cloud Console i descarrega el fitxer `oauth_client_id.json` dins `app/`. **Nota:** la configuració gratuïta actual només admet 2 dispositius; per a més cal actualitzar el pla de Google.
4. **Configura l’API backend**  
   *A `ApiService.kt` pots sobreescriure `BASE_URL` o bé afegir‑la a
   `gradle.properties` en forma de `backendUrl=` i llegir‑la amb
   `local.properties`.*

5. Compila i executa:

   ```bash
   ./gradlew :app:installDebug
   # o simplement F9 / "Run" des d’Android Studio
   ```

---

## 🏗️ Arquitectura

| Capa | Tecnologies | Descripció |
|------|-------------|------------|
| **UI** | Jetpack Compose, Material 3 | Pantalles declaratives, gestió d’estat amb `remember` + `ViewModel` |
| **Navegació** | Navigation Compose | `Routes.kt` defineix rutes, `NavHost` centralitzat |
| **Dominis / Model** | Data classes Kotlin | `Calendar`, `Schedule`, `TaskCategory`, `UserSettings`… |
| **Persistència** | (Pròximament Room) | Actualment tota la dada ve de l’API REST |
| **Networking** | Retrofit 2, Gson | Serialitzadors personalitzats (`ZonedDateTimeAdapter`) |
| **DI (light)** | `object` singletons | Sense Hilt per simplicitat del projecte |
| **Auth** | Google Identity Services (OAuth 2) | Flux d’email/contrasenya i OAuth 2 (sense Firebase) |

---

## 📸 Pantalles principals

| Ruta | Vista |
|------|-------|
| `welcome` | Pantalla de benvinguda + registre / login |
| `home` | Resum d’avui i accés ràpid |
| `calendar` | Vista mensual amb filtres de categories |
| `schedule` | Planificador setmanal i tasques repetitives |
| `today_tasks` | Focus en les tasques del dia |
| `shopping_list` | **Pròximament** — placeholder "Coming Soon" |
| `exercises` | **Pròximament** — placeholder "Coming Soon" |
| `profile_settings` | Tema, idioma, (Pròximament) notificacions, canviar contrasenya |
| `profile` | Vista de perfil d'usuari |
| `edit_profile` | Formulari per editar dades personals |
| `settings` | Ajustos generals de l'app |
| `contact` | Pantalla de contacte i enllaços de suport |
| `about_us` | Informació sobre l'equip i el projecte |

---

## 🧪 Tests

* **Unit tests (JUnit 4)**  
  ```bash
  ./gradlew test
  ```
* **Instrumented tests (Espresso 3.6.1)**  
  ```bash
  ./gradlew connectedAndroidTest
  ```

---

## 🤝 Contribució

1. Fes un *fork* i crea una branca descriptiva: `git checkout -b feature/nom‑funcionalitat`
2. Assegura’t que **gradlew spotlessApply ktlintFormat** no retorna errors.
3. Obre un *pull request* amb una descripció clara del canvi.

---

## 📄 Llicència

Aquest projecte s’entrega **sense llicència**.  
Si vols reutilitzar el codi, obre primer una *issue* per discutir‑ho.

---

## 🙌 Crèdits

**G7 Margaret Hamilton**  
Projecte escolar de Desenvolupament d’Aplicacions Multiplataforma (DAM) – Curs 2024‑2025.

> “There’s no such thing as luck. Luck is where preparation meets opportunity.”  
> — *Margaret Hamilton*
