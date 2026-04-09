# Troubleshooting Guide - Request Timeout Issue

## 🔴 Problem: "Request timeout" Error

You're seeing this error:
```json
{
  "content": null,
  "agentType": "CODE_GENERATION",
  "timestamp": "2026-04-09T13:43:46.173379800Z",
  "error": "Failed to generate code: Request timeout",
  "metadata": {}
}
```

---

## 🔍 Root Causes

### 1. Invalid or Test API Key
The NVIDIA_API_KEY might be:
- A test/placeholder key (like "test-key-123")
- Expired or revoked
- Not authorized for the model being used

### 2. Network Connectivity
- Firewall blocking HTTPS requests to NVIDIA API
- Proxy configuration needed
- DNS resolution issues

### 3. NVIDIA API Issues
- API endpoint is slow or overloaded
- Model is not available
- Rate limiting

---

## ✅ Solutions

### Solution 1: Verify Your API Key

1. **Check if you have a real NVIDIA API key:**
   - Go to https://build.nvidia.com/
   - Sign in and get your API key
   - Make sure it's not a test key

2. **Set the correct API key:**

**Windows PowerShell:**
```powershell
$env:NVIDIA_API_KEY = "nvapi-YOUR-ACTUAL-KEY-HERE"
```

**Windows CMD:**
```cmd
set NVIDIA_API_KEY=nvapi-YOUR-ACTUAL-KEY-HERE
```

3. **Restart the application:**
```bash
# Stop current process
Get-Process java | Stop-Process -Force

# Rebuild and restart
mvn clean package -DskipTests
java -jar target/multi-agent-assistant-1.0.0.jar --server.port=8081
```

---

### Solution 2: Test NVIDIA API Directly

Test if you can reach the NVIDIA API:

```bash
curl -X POST https://integrate.api.nvidia.com/v1/chat/completions \
  -H "Authorization: Bearer YOUR-API-KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "google/gemma-4-31b-it",
    "messages": [{"role": "user", "content": "Hello"}],
    "max_tokens": 100
  }'
```

**Expected**: You should get a response within 5-10 seconds

**If it times out**: Network or API key issue

---

### Solution 3: Increase Timeout (Temporary Fix)

If the API is just slow, increase the timeout:

1. **Set environment variable:**
```powershell
$env:REQUEST_TIMEOUT_SECONDS = "120"
```

2. **Restart the application**

---

### Solution 4: Use Mock Mode for Testing

For testing the application without a real API key, you can:

1. **Test with shorter timeout to fail fast:**
```powershell
$env:REQUEST_TIMEOUT_SECONDS = "10"
```

2. **Focus on testing the API structure:**
   - Test validation (empty prompts should return 400)
   - Test health endpoint (should always work)
   - Test request/response format

---

## 🧪 Quick Diagnostic Tests

### Test 1: Health Endpoint (Should Always Work)
```bash
curl http://localhost:8081/api/v1/health
```
**Expected**: `{"status":"UP"}` immediately

### Test 2: Validation Error (Should Work Without API)
```bash
curl -X POST http://localhost:8081/api/v1/assist \
  -H "Content-Type: application/json" \
  -d '{"prompt":"","agentType":"CODE_GENERATION","streaming":false}'
```
**Expected**: HTTP 400 with validation error

### Test 3: Check Application Logs
Look for these log entries:
```
INFO  c.a.multiagent.config.AppConfig - NVIDIA_API_KEY: [CONFIGURED]
INFO  c.a.m.client.NvidiaApiClient - Sending request to NVIDIA API
ERROR ... TimeoutException: Did not observe any item or terminal signal within 60000ms
```

---

## 🔧 Alternative: Use Different Model

If the current model is unavailable, try a different one:

```powershell
$env:NVIDIA_MODEL = "meta/llama-3.1-8b-instruct"
```

Or:
```powershell
$env:NVIDIA_MODEL = "mistralai/mixtral-8x7b-instruct-v0.1"
```

Then restart the application.

---

## 📊 Understanding the Timeout

The application has a 60-second timeout by default:
- **0-5 seconds**: Normal response time
- **5-30 seconds**: Slow but acceptable
- **30-60 seconds**: Very slow, might timeout
- **60+ seconds**: Timeout error

---

## 🎯 Recommended Actions (In Order)

1. ✅ **Verify you have a real NVIDIA API key** (not "test-key")
2. ✅ **Test the API directly** with curl to confirm connectivity
3. ✅ **Check firewall/proxy settings** if curl also times out
4. ✅ **Try a different model** if the current one is unavailable
5. ✅ **Increase timeout** as a temporary workaround
6. ✅ **Contact NVIDIA support** if API is consistently slow

---

## 💡 Working Without Real API Key

If you don't have a real API key yet, you can still:

1. **Test the application structure:**
   - Health endpoint works
   - Validation works
   - Error handling works
   - Swagger UI works

2. **Test with mock responses:**
   - The application correctly formats requests
   - The application correctly handles timeouts
   - The application logs properly

3. **Get a real API key:**
   - Visit https://build.nvidia.com/
   - Sign up for free tier
   - Get your API key
   - Set it in environment variable

---

## 🚨 Common Mistakes

### Mistake 1: Using Test Key
```powershell
# ❌ Wrong
$env:NVIDIA_API_KEY = "test-key-123"

# ✅ Correct
$env:NVIDIA_API_KEY = "nvapi-abc123def456..."
```

### Mistake 2: Not Restarting After Changing Key
After setting a new API key, you MUST restart the application:
```powershell
Get-Process java | Stop-Process -Force
java -jar target/multi-agent-assistant-1.0.0.jar --server.port=8081
```

### Mistake 3: Wrong API URL
The default URL should be:
```
https://integrate.api.nvidia.com/v1/chat/completions
```

---

## 📞 Still Having Issues?

### Check These:

1. **Application logs** - Look for specific error messages
2. **Network connectivity** - Can you reach other HTTPS sites?
3. **API key validity** - Is it the correct format? (starts with "nvapi-")
4. **Model availability** - Is the model you're using available?

### Get Help:

1. **NVIDIA API Documentation**: https://docs.api.nvidia.com/
2. **NVIDIA Developer Forums**: https://forums.developer.nvidia.com/
3. **Check API Status**: https://status.nvidia.com/

---

## ✅ Success Indicators

You'll know it's working when:
- Response comes back in 2-5 seconds
- `content` field has actual generated text (not null)
- `error` field is null
- Logs show successful API call

**Example successful response:**
```json
{
  "content": "Here's a Java function...",
  "agentType": "CODE_GENERATION",
  "timestamp": "2026-04-09T13:45:00Z",
  "error": null,
  "metadata": {
    "model": "google/gemma-4-31b-it"
  }
}
```

---

## 🎓 Next Steps

Once you have a valid API key:
1. Set the environment variable
2. Restart the application
3. Try the examples in SWAGGER_TESTING_GUIDE.md
4. Enjoy AI-powered coding assistance!

---

**Need more help?** Check the main README.md or application logs for more details.
