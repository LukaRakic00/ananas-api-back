# Render Deployment Uputstva

## Priprema za Render

Projekat je spreman za deployment na Render. Evo šta treba da uradite:

### 1. Environment Variables u Render Dashboard-u

Kada kreiramo novi Web Service na Render-u, potrebno je postaviti sledeće Environment Variables:

**Obavezno:**
- `SUPABASE_DB_URL` - Kompletan JDBC URL (npr: `jdbc:postgresql://aws-1-eu-north-1.pooler.supabase.com:6543/postgres`)
- `SUPABASE_DB_USERNAME` - Supabase username
- `SUPABASE_DB_PASSWORD` - Supabase password

**Ili pojedinačno:**
- `SUPABASE_DB_HOST` - Supabase host
- `SUPABASE_DB_PORT` - Supabase port (6543 za pooler, 5432 za direct)
- `SUPABASE_DB_NAME` - Database name (obično `postgres`)
- `SUPABASE_DB_USERNAME` - Supabase username
- `SUPABASE_DB_PASSWORD` - Supabase password

**Automatski (Render postavlja):**
- `PORT` - Render automatski postavlja port

### 2. Build Settings u Render Dashboard-u

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

### 3. Alternativa: Korišćenje render.yaml

Ako koristite `render.yaml` fajl (već je kreiran), Render će automatski prepoznati konfiguraciju.

### 4. Docker Deployment (Alternativa)

Ako želite da koristite Dockerfile (već je kreiran):

**Build Command:**
```bash
docker build -t excel-to-xml .
```

**Start Command:**
```bash
docker run -p $PORT:8080 -e SUPABASE_DB_URL=$SUPABASE_DB_URL -e SUPABASE_DB_USERNAME=$SUPABASE_DB_USERNAME -e SUPABASE_DB_PASSWORD=$SUPABASE_DB_PASSWORD excel-to-xml
```

### 5. Provera nakon deployment-a

Nakon što se aplikacija deploy-uje, proverite:

1. **Health Check:**
   ```
   GET https://your-app.onrender.com/api/excel
   ```

2. **Test Upload:**
   ```
   POST https://your-app.onrender.com/api/excel/upload
   ```

### 6. Važne napomene

- ✅ `.env` fajl se **NE** koristi na Render-u - koristite Environment Variables u dashboard-u
- ✅ `PORT` se automatski postavlja od strane Render-a
- ✅ CORS je već konfigurisan za sve origin-e
- ✅ Aplikacija koristi HikariCP connection pooling
- ✅ File upload limit je 10MB (možete povećati u `application.properties`)

### 7. Troubleshooting

**Problem: Database connection failed**
- Proverite da li su sve environment variables postavljene
- Proverite da li je Supabase database dostupan
- Proverite da li je port tačan (6543 za pooler, 5432 za direct)

**Problem: Application won't start**
- Proverite build logs u Render dashboard-u
- Proverite da li je Java 17 dostupan
- Proverite da li su sve dependency-ji pravilno učitani

**Problem: Port binding error**
- Render automatski postavlja PORT - ne menjajte ga
- Aplikacija automatski koristi `${PORT:8080}` iz `application.properties`

