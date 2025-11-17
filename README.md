# Excel to XML API

Spring Boot REST API aplikacija za učitavanje Excel fajlova u Supabase PostgreSQL bazu, sa mogućnošću pregleda, pretrage i exporta kao XML.

## 🚀 Funkcionalnosti

- ✅ Upload Excel fajlova (.xlsx, .xls)
- ✅ Automatsko učitavanje podataka u Supabase PostgreSQL bazu
- ✅ Pregled podataka sa paginacijom
- ✅ Napredna pretraga i filtriranje
- ✅ CRUD operacije (Create, Read, Update, Delete)
- ✅ Export podataka kao XML
- ✅ Dva endpointa: jedan za frontend (CRUD), jedan za Ananas sistem (READ only)
- ✅ CORS podrška
- ✅ Environment variables podrška (.env fajl)

## 📋 Preduslovi

- Java 17 ili noviji
- Maven 3.6+
- Supabase PostgreSQL baza
- (Opciono) Docker za container deployment

## 🔧 Lokalna Instalacija

### 1. Kloniranje repozitorijuma

```bash
git clone <repository-url>
cd back
```

### 2. Kreiranje .env fajla

Kreirajte `.env` fajl u root direktorijumu projekta:

```env
# Supabase Database Configuration
# Opcija 1: Kompletan URL sa portom (preporučeno)
SUPABASE_DB_URL=jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:6543/postgres
SUPABASE_DB_USERNAME=postgres.xxffgsskzydabepyvhya
SUPABASE_DB_PASSWORD=your_password_here

# Opcija 2: Pojedinačne varijable sa portom (alternativa)
# SUPABASE_DB_HOST=aws-1-eu-north-1.pooler.supabase.com
# SUPABASE_DB_PORT=6543
# SUPABASE_DB_NAME=postgres
# SUPABASE_DB_USERNAME=postgres.xxffgsskzydabepyvhya
# SUPABASE_DB_PASSWORD=your_password_here
```

**Napomena:** 
- Za **Transaction pooler** koristite port **6543**
- Za **Direct connection** koristite port **5432**
- Zamenite vrednosti sa vašim Supabase podacima

### 3. Kreiranje tabele u Supabase

Izvršite sledeći SQL u Supabase SQL Editor-u:

```sql
CREATE TABLE excel_rows (
    id SERIAL PRIMARY KEY,
    upload_id UUID NOT NULL DEFAULT gen_random_uuid(),
    naziv VARCHAR(255) NOT NULL,
    vrednost VARCHAR(255),
    napomena TEXT,
    row_number INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_upload_id ON excel_rows(upload_id);
CREATE INDEX idx_created_at ON excel_rows(created_at);
```

### 4. Pokretanje aplikacije

```bash
mvn spring-boot:run
```

Aplikacija će se pokrenuti na `http://localhost:8080`

## 📚 API Dokumentacija

### Frontend Endpointi (CRUD)

**Base URL (Produkcija):** `https://ananas-api-back.onrender.com/api/excel`  
**Base URL (Lokalno):** `http://localhost:8080/api/excel`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| POST | `/api/excel/upload` | Upload Excel fajla |
| GET | `/api/excel` | Pregled svih redova (sa paginacijom) |
| GET | `/api/excel/{id}` | Pregled reda po ID-u |
| GET | `/api/excel/upload/{uploadId}` | Pregled redova po upload ID-u |
| POST | `/api/excel/search` | Pretraga (POST) |
| GET | `/api/excel/search` | Pretraga (GET) |
| POST | `/api/excel` | Kreiranje novog reda |
| PUT | `/api/excel/{id}` | Ažuriranje reda |
| DELETE | `/api/excel/{id}` | Brisanje reda |
| DELETE | `/api/excel` | Brisanje svih redova |
| GET | `/api/excel/export/xml` | Export kao XML |
| GET | `/api/excel/export/xml/{uploadId}` | Export kao XML po upload ID-u |

### Ananas Endpointi (READ only)

**Base URL (Produkcija):** `https://ananas-api-back.onrender.com/api/ananas/excel`  
**Base URL (Lokalno):** `http://localhost:8080/api/ananas/excel`

| Metoda | Endpoint | Opis |
|--------|----------|------|
| GET | `/api/ananas/excel` | Pregled svih redova |
| GET | `/api/ananas/excel/{id}` | Pregled reda po ID-u |
| GET | `/api/ananas/excel/upload/{uploadId}` | Pregled redova po upload ID-u |
| GET | `/api/ananas/excel/search` | Pretraga |
| GET | `/api/ananas/excel/filter` | Filter |
| GET | `/api/ananas/excel/export/xml` | Export kao XML |
| GET | `/api/ananas/excel/export/xml/{uploadId}` | Export kao XML po upload ID-u |

**⚠️ Važno:** Frontend koristi samo `/api/excel/*` endpoint. `/api/ananas/excel/*` je rezervisan za Ananas sistem.

Detaljna dokumentacija sa primerima koda se nalazi u:
- `FRONTEND_INSTRUCTIONS.md` - Uputstva za frontend tim
- `FRONTEND_API_DOCUMENTATION.md` - Detaljna API dokumentacija

## 📄 Format Excel Fajla

Excel fajl treba da ima sledeću strukturu:

| Kolona A | Kolona B | Kolona C |
|----------|----------|----------|
| Naziv (obavezno) | Vrednost (opciono) | Napomena (opciono) |

**Napomena:** Prvi red se preskače (header).

## 🐳 Docker Deployment

### Build Docker image

```bash
docker build -t excel-to-xml .
```

### Run Docker container

```bash
docker run -p 8080:8080 \
  -e SUPABASE_DB_URL=jdbc:postgresql://host:port/database \
  -e SUPABASE_DB_USERNAME=username \
  -e SUPABASE_DB_PASSWORD=password \
  excel-to-xml
```

## ☁️ Render Deployment

Projekat je spreman za deployment na Render. Detaljna uputstva se nalaze u `RENDER_DEPLOYMENT.md`.

### Brzi start na Render-u:

1. Push kod na GitHub/GitLab
2. U Render Dashboard-u: New → Web Service
3. Povežite repository
4. Render će automatski prepoznati `render.yaml`
5. Postavite Environment Variables:
   - `SUPABASE_DB_URL` (ili pojedinačne varijable)
   - `SUPABASE_DB_USERNAME`
   - `SUPABASE_DB_PASSWORD`

**Build Command:** `mvn clean package -DskipTests`  
**Start Command:** `java -jar target/*.jar`  
**Environment:** Java 17

## 🛠️ Tehnologije

- **Spring Boot** 3.5.7
- **PostgreSQL** (Supabase)
- **Spring Data JPA** - ORM i database access
- **Apache POI** - Excel file processing
- **Jackson** - XML export
- **Lombok** - Code generation
- **HikariCP** - Connection pooling

## 📦 Zavisnosti

Glavne zavisnosti definisane u `pom.xml`:

- `spring-boot-starter-web` - Web framework
- `spring-boot-starter-data-jpa` - JPA support
- `postgresql` - PostgreSQL driver
- `poi` & `poi-ooxml` - Excel processing
- `jackson-dataformat-xml` - XML export
- `lombok` - Code generation

## 🔍 Primeri Korišćenja

### Upload Excel fajla (JavaScript)

```javascript
const formData = new FormData();
formData.append('file', fileInput.files[0]);

const response = await fetch('https://ananas-api-back.onrender.com/api/excel/upload', {
  method: 'POST',
  body: formData
});
const data = await response.json();
console.log(data);
```

### Pregled svih redova

```bash
# Produkcija
curl https://ananas-api-back.onrender.com/api/excel?page=0&size=20

# Lokalno
curl http://localhost:8080/api/excel?page=0&size=20
```

### Pretraga

```bash
# Produkcija
curl "https://ananas-api-back.onrender.com/api/excel/search?search=tekst&page=0&size=20"

# Lokalno
curl "http://localhost:8080/api/excel/search?search=tekst&page=0&size=20"
```

### Kreiranje novog reda

```bash
# Produkcija
curl -X POST https://ananas-api-back.onrender.com/api/excel \

# Lokalno
curl -X POST http://localhost:8080/api/excel \
  -H "Content-Type: application/json" \
  -d '{
    "naziv": "Test naziv",
    "vrednost": "Test vrednost",
    "napomena": "Test napomena",
    "rowNumber": 1
  }'
```

## 🧪 Testiranje

```bash
# Pokretanje testova
mvn test

# Pokretanje aplikacije sa testovima
mvn spring-boot:run
```

## 📝 Konfiguracija

### application.properties

Glavne konfiguracije se nalaze u `src/main/resources/application.properties`:

- Server port: `${PORT:8080}` (Render automatski postavlja PORT)
- Database konfiguracija: Učitava se iz environment variables
- File upload limit: 10MB
- JPA: Hibernate sa PostgreSQL dialect

### Environment Variables

Aplikacija podržava učitavanje varijabli iz `.env` fajla lokalno, ili kroz environment variables na production serveru.

## 🐛 Troubleshooting

### Problem: Database connection failed

- Proverite da li su sve environment variables postavljene
- Proverite da li je Supabase database dostupan
- Proverite da li je port tačan (6543 za pooler, 5432 za direct)

### Problem: Application won't start

- Proverite da li je Java 17 instaliran
- Proverite da li su sve dependency-ji pravilno učitani
- Proverite build logs

### Problem: File upload fails

- Proverite da li je fajl u .xlsx ili .xls formatu
- Proverite da li je fajl manji od 10MB
- Proverite format Excel fajla (prva kolona mora biti "Naziv")

## 📄 Licenca

Ovaj projekat je kreiran za Ananas sistem.

## 👥 Autor

Ananas Development Team

## 📞 Podrška

Za dodatne informacije i podršku, pogledajte:
- `FRONTEND_INSTRUCTIONS.md` - Uputstva za frontend tim
- `RENDER_DEPLOYMENT.md` - Uputstva za Render deployment
- `FRONTEND_API_DOCUMENTATION.md` - Detaljna API dokumentacija
