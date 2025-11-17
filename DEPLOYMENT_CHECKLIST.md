# ✅ Deployment Checklist za Render

## Pre Deployment-a

### 1. ✅ Kod je spreman
- [x] Sve klase su kreirane i kompajliraju se bez grešaka
- [x] Specification API je implementiran za filtere
- [x] Excel export i XML export su implementirani
- [x] CRUD operacije su implementirane
- [x] READ-only endpoint za Ananas je implementiran

### 2. ✅ Konfiguracija
- [x] `application.properties` - ✅ Konfigurisan za Render (PORT env variable)
- [x] `render.yaml` - ✅ Kreiran sa svim potrebnim environment variables
- [x] `Dockerfile` - ✅ Kreiran (opciono za Docker deployment)
- [x] `.dockerignore` - ✅ Kreiran

### 3. ✅ Database
- [x] SQL migracija (`database_migration.sql`) - ✅ Kreirana
- [x] Tabela `merchant_inventory` mora biti kreirana u Supabase pre deployment-a

### 4. ✅ Dokumentacija
- [x] `README.md` - ✅ Ažuriran
- [x] `RENDER_DEPLOYMENT.md` - ✅ Kreiran sa instrukcijama
- [x] `FRONTEND_MERCHANT_INVENTORY.md` - ✅ Kreiran
- [x] `API_ENDPOINTS_SUMMARY.md` - ✅ Kreiran

---

## 🚀 Koraci za Deployment na Render

### Korak 1: Kreiranje Web Service-a na Render-u

1. Idite na [Render Dashboard](https://dashboard.render.com)
2. Kliknite na **"New +"** → **"Web Service"**
3. Povežite GitHub repozitorijum
4. Odaberite branch (obično `main` ili `master`)

### Korak 2: Konfiguracija Build Settings

**Build Command:**
```bash
mvn clean package -DskipTests
```

**Start Command:**
```bash
java -jar target/*.jar
```

**Environment:**
- Java 17

**Ili koristite `render.yaml`** - Render će automatski prepoznati konfiguraciju.

### Korak 3: Environment Variables

Postavite sledeće Environment Variables u Render Dashboard-u:

**Obavezno (jedna od opcija):**

**Opcija 1 - Kompletan URL:**
```
SUPABASE_DB_URL=jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:6543/postgres
SUPABASE_DB_USERNAME=postgres.xxffgsskzydabepyvhya
SUPABASE_DB_PASSWORD=your_password_here
```

**Opcija 2 - Pojedinačne varijable:**
```
SUPABASE_DB_HOST=aws-1-eu-north-1.pooler.supabase.com
SUPABASE_DB_PORT=6543
SUPABASE_DB_NAME=postgres
SUPABASE_DB_USERNAME=postgres.xxffgsskzydabepyvhya
SUPABASE_DB_PASSWORD=your_password_here
```

**Napomena:** `PORT` se automatski postavlja od strane Render-a - ne postavljajte ga ručno!

### Korak 4: Database Setup

**Pre deployment-a, izvršite SQL migraciju u Supabase:**

1. Idite na Supabase Dashboard → SQL Editor
2. Kopirajte i izvršite SQL iz `database_migration.sql` fajla
3. Proverite da li je tabela `merchant_inventory` kreirana

### Korak 5: Deploy

1. Kliknite na **"Create Web Service"**
2. Render će automatski:
   - Klonirati kod
   - Instalirati zavisnosti
   - Build-ovati aplikaciju
   - Pokrenuti aplikaciju

### Korak 6: Provera nakon Deployment-a

**1. Health Check:**
```bash
GET https://your-app.onrender.com/
```

Očekivani odgovor:
```json
{
  "message": "Dobrodošli na Excel to XML API!",
  "frontend_api_base_url_production": "https://your-app.onrender.com/api/excel",
  ...
}
```

**2. Test Endpoint:**
```bash
GET https://your-app.onrender.com/api/excel?page=0&size=20
```

**3. Test Upload (sa Excel fajlom):**
```bash
POST https://your-app.onrender.com/api/excel/upload
Content-Type: multipart/form-data
file: [Excel fajl]
```

---

## ⚠️ Važne Napomene

1. **`.env` fajl se NE koristi na Render-u** - koristite Environment Variables u dashboard-u
2. **`PORT` se automatski postavlja** - ne menjajte ga
3. **Database mora biti kreiran pre deployment-a** - izvršite `database_migration.sql`
4. **CORS je konfigurisan** za sve origin-e
5. **File upload limit je 10MB** - može se povećati u `application.properties`

---

## 🔍 Troubleshooting

### Problem: Application won't start
- Proverite build logs u Render dashboard-u
- Proverite da li je Java 17 dostupan
- Proverite da li su sve dependency-ji pravilno učitani

### Problem: Database connection failed
- Proverite da li su sve environment variables postavljene
- Proverite da li je Supabase database dostupan
- Proverite da li je port tačan (6543 za pooler, 5432 za direct)
- Proverite da li je tabela `merchant_inventory` kreirana

### Problem: 404 errors
- Proverite da li je aplikacija pokrenuta
- Proverite da li su endpointi pravilno mapirani
- Proverite Render logs za detalje

### Problem: 500 errors
- Proverite Render logs za stack trace
- Proverite da li je database pravilno konfigurisan
- Proverite da li su sve environment variables postavljene

---

## 📝 Finalni Checklist

- [ ] GitHub repozitorijum je povezan sa Render-om
- [ ] Environment variables su postavljene u Render dashboard-u
- [ ] Database migracija je izvršena u Supabase
- [ ] Build command je postavljen: `mvn clean package -DskipTests`
- [ ] Start command je postavljen: `java -jar target/*.jar`
- [ ] Java 17 je odabran kao environment
- [ ] Aplikacija je deploy-ovana
- [ ] Health check endpoint vraća odgovor
- [ ] Test endpoint vraća podatke
- [ ] Upload endpoint radi sa Excel fajlom

---

## 🎉 Srećan Deployment!

Ako imate problema, proverite:
1. Render logs
2. Supabase connection
3. Environment variables
4. Database schema

Sve je spremno za deployment! 🚀

