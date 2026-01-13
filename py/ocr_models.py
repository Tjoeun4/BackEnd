import numpy as np
import cv2
from paddleocr import PaddleOCR

class OCRHandler:
    def __init__(self):
        print("Loading PaddleOCR...")
        # 2026년 최신 버전 안정화 설정
        self.paddle_ocr = PaddleOCR(use_angle_cls=True, lang='korean')

    def process_paddle(self, image_bytes):
        try:
            nparr = np.frombuffer(image_bytes, np.uint8)
            img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
            
            if img is None:
                return []

            # OCR 실행
            result = self.paddle_ocr.ocr(img) 
            
            # 결과가 없거나 None인 경우 처리
            if not result or result[0] is None:
                print("OCR 결과가 비어있습니다.")
                return []
            
            final_results = []
            # PaddleOCR의 다중 리스트 구조를 안전하게 순회
            for line in result:
                for res in line:
                    try:
                        # res[1][0]: 텍스트, res[1][1]: 신뢰도, res[0]: 좌표
                        text = res[1][0]
                        conf = float(res[1][1])
                        bbox = res[0]
                        
                        final_results.append({
                            "text": text,
                            "bbox": bbox,
                            "conf": conf
                        })
                    except (IndexError, TypeError) as e:
                        # 예상치 못한 구조의 데이터는 건너뜀
                        continue
            
            return final_results
            
        except Exception as e:
            print(f"OCR 처리 중 치명적 에러: {e}")
            return []