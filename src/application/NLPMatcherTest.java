package application;

import java.util.Map;

public class NLPMatcherTest {
    
    public static void main(String[] args) {
        // Sample CV text
        String cvText = """
            John Doe
            Software Developer
            
            EXPERIENCE
            Senior Java Developer at TechCorp (2019-2023)
            - Developed web applications using Java, Spring Boot, and Hibernate
            - Worked with MySQL databases and REST APIs
            - Led a team of 3 junior developers
            - 4 years of experience in Java development
            
            Junior Developer at StartupXYZ (2017-2019)
            - Built frontend applications using HTML, CSS, and JavaScript
            - Basic knowledge of React and Angular
            - 2 years of experience in web development
            
            SKILLS
            - Java (proficient)
            - Spring Framework (expert)
            - SQL and MySQL (proficient)
            - JavaScript (basic knowledge)
            - HTML/CSS (intermediate)
            - Git and version control
            - Team leadership and mentoring
            """;
        
        // Sample job description
        String jobDescription = """
            Senior Java Developer
            
            We are looking for a Senior Java Developer with:
            - 5+ years of experience in Java development
            - Expert knowledge of Spring Framework and Hibernate
            - Strong experience with SQL databases (MySQL, PostgreSQL)
            - Experience with REST APIs and microservices
            - Knowledge of JavaScript and frontend technologies
            - Leadership experience and team mentoring skills
            - Experience with cloud platforms (AWS, Azure)
            - DevOps knowledge (Docker, Kubernetes)
            
            Responsibilities:
            - Lead development of enterprise applications
            - Mentor junior developers
            - Design and implement scalable solutions
            - Collaborate with cross-functional teams
            """;
        
        // Test the NLP matching
        System.out.println("=== NLP-Based CV-Job Matching Test ===\n");
        
        NLPMatcher.MatchResult result = NLPMatcher.analyzeCVAndJob(cvText, jobDescription);
        
        System.out.println("Overall Match Score: " + String.format("%.2f", result.getOverallScore() * 100) + "%");
        System.out.println("Experience Level: " + result.getExperienceLevel());
        System.out.println("Experience Gap: " + result.getExperienceMatch() + " years");
        
        System.out.println("\n=== Skill Analysis ===");
        System.out.println("Matched Skills:");
        for (String skill : result.getMatchedSkills()) {
            System.out.println("  ✓ " + skill);
        }
        
        System.out.println("\nMissing Skills:");
        for (String skill : result.getMissingSkills()) {
            System.out.println("  ✗ " + skill);
        }
        
        System.out.println("\n=== Detailed Skill Scores ===");
        for (Map.Entry<String, Double> entry : result.getSkillScores().entrySet()) {
            System.out.println(entry.getKey() + ": " + String.format("%.2f", entry.getValue() * 100) + "%");
        }
        
        System.out.println("\n=== Recommendations ===");
        for (String recommendation : result.getRecommendations()) {
            System.out.println("• " + recommendation);
        }
        
        System.out.println("\n=== Analysis Summary ===");
        if (result.getOverallScore() >= 0.8) {
            System.out.println("🎉 Excellent match! This candidate is well-suited for the position.");
        } else if (result.getOverallScore() >= 0.6) {
            System.out.println("👍 Good match with some areas for improvement.");
        } else if (result.getOverallScore() >= 0.4) {
            System.out.println("⚠️  Fair match. Consider additional training or experience.");
        } else {
            System.out.println("❌ Poor match. Significant skill gaps identified.");
        }
    }
} 