package com.example.project.global.config;

import java.util.Map;

// Spring 프레임워크에서 제공하는 HTTP 통신 및 파일 처리를 위한 라이브러리들
import org.springframework.http.MediaType; // 전송할 데이터의 형식(MIME Type)을 지정하기 위해 사용
import org.springframework.http.client.MultipartBodyBuilder; // 이미지 파일 + 텍스트 데이터를 하나의 '폼'으로 묶어주는 도구
import org.springframework.stereotype.Service; // 이 클래스를 스프링의 서비스 빈으로 등록
import org.springframework.web.multipart.MultipartFile; // 사용자가 업로드한 실제 이미지 파일 객체
import org.springframework.web.reactive.function.BodyInserters; // HTTP 요청 본문(Body)에 데이터를 삽입할 때 사용
import org.springframework.web.reactive.function.client.WebClient; // 비동기 기반의 최신 HTTP 클라이언트

// Project Reactor 라이브러리: 비동기 데이터 스트림을 다루기 위한 객체
import reactor.core.publisher.Mono; // 결과값을 0개 또는 1개만 포함하는 비동기 컨테이너 (결과가 올 때까지 기다리지 않고 통로만 확보함)

/**
 * AI 서버(FastAPI)와 통신하여 영수증 OCR 분석 결과를 받아오는 클라이언트 클래스
 */
@Service
public class OcrClient {
    
    // HTTP 요청을 실제로 수행할 객체
    private final WebClient webClient;

    /**
     * 생성자: WebClient의 기본 설정을 수행합니다.
     */
    public OcrClient() {
        // FastAPI 서버의 주소를 기본값으로 설정하여 WebClient 객체 생성
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8000") 
                .build();
    }

    /**
     * 영수증 이미지를 분석 서버로 보내고 분석 결과(JSON -> Map)를 반환받습니다.
     * * @param file      사용자가 업로드한 영수증 이미지 파일
     * @param modelType 사용할 AI 모델 종류 ("donut" 또는 "layoutlm")
     * @return Mono<Map> 분석된 데이터가 담길 비동기 래퍼 객체 (성공 시 Map 형태로 데이터 반환)
     */
    public Mono<Map> analyzeReceipt(MultipartFile file, String modelType) {
        
        // 1. 멀티파트 폼 데이터 구축 (HTML의 <form enctype="multipart/form-data">와 같은 역할)
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        
        // "file"이라는 이름의 파트에 실제 이미지 리소스(데이터 스트림)를 담습니다.
        // FastAPI 서버에서는 'file'이라는 변수명으로 이 데이터를 받게 됩니다.
        builder.part("file", file.getResource());

        // 2. 비동기 POST 요청 실행
        return webClient.post()
                // URI 경로 결정: 예) http://localhost:8000/analyze/donut
                .uri("/analyze/" + modelType) 
                
                // 요청 헤더 설정: 파일 전송임을 명시
                .contentType(MediaType.MULTIPART_FORM_DATA)
                
                // 요청 본문(Body)에 아까 만든 멀티파트 폼 데이터를 삽입
                .body(BodyInserters.fromMultipartData(builder.build()))
                
                // 요청을 보내고 응답을 추출하기 위한 단계 시작
                .retrieve()
                
                // 서버로부터 받은 JSON 응답을 자바의 Map 구조로 자동 변환(Mapping)
                // Mono<Map> 형태로 반환하여 호출한 곳에서 비동기로 처리할 수 있게 함
                .bodyToMono(Map.class);
    }
}