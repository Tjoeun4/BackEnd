from fastapi import FastAPI, UploadFile, File, HTTPException
from ocr_models import OCRHandler
import uvicorn

app = FastAPI(title="PaddleOCR Receipt Server")
ocr_handler = OCRHandler()

@app.post("/analyze/receipt")
async def analyze_receipt(file: UploadFile = File(...)):
    try:
        content = await file.read()
        # PaddleOCR 실행
        result = ocr_handler.process_paddle(content)
        return {"status": "success", "data": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)