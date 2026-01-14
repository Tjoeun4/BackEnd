# test_ocr.py
import os
from ocr_models import OCRHandler

def run_test():
    print("--- [PaddleOCR] 테스트 시작 ---")
    handler = OCRHandler()
    
    # 반드시 실제 이미지가 있는 경로로 설정하세요.
    image_path = "test_images/receipt.jpg" 
    
    if not os.path.exists(image_path):
        print(f"파일이 없습니다: {os.path.abspath(image_path)}")
        return

    try:
        # 바이트 전달 방식 대신 '경로'를 직접 전달하여 테스트
        results = handler.process_paddle(image_path)
        
        print("\n--- 인식 결과 ---")
        if not results:
            print("인식된 글자가 없습니다. 이미지 화질이나 각도를 확인하세요.")
        else:
            for i, item in enumerate(results):
                print(f"[{i+1}] {item['text']} (신뢰도: {item['conf']:.2f})")
                
    except Exception as e:
        print(f"테스트 실행 에러: {e}")

if __name__ == "__main__":
    run_test()