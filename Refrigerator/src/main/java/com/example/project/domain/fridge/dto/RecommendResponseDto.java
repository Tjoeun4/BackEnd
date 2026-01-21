package com.example.project.domain.fridge.dto;

import java.util.List;

public class RecommendResponseDto {

    private List<Recommendation> recommendations;

    public RecommendResponseDto(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public static class Recommendation {

        private String title;
        private List<String> useItems;
        private List<String> steps;
        private Integer estimatedMinutes;
        private List<String> urgentItemsUsed;
        private List<String> extraPurchase;

        public String getTitle() {
            return title;
        }

        public List<String> getUseItems() {
            return useItems;
        }

        public List<String> getSteps() {
            return steps;
        }

        public Integer getEstimatedMinutes() {
            return estimatedMinutes;
        }

        public List<String> getUrgentItemsUsed() {
            return urgentItemsUsed;
        }

        public List<String> getExtraPurchase() {
            return extraPurchase;
        }
    }
}
