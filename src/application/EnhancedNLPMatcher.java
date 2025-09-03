package application;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;

import java.util.*;
import java.util.regex.Pattern;

public class EnhancedNLPMatcher {
    
    private static Tokenizer tokenizer;
    
    // Skill-related indicators
    private static final Set<String> SKILL_INDICATORS = new HashSet<>(Arrays.asList(
        "experience", "years", "expertise", "proficient", "skilled", "expert", "senior", "junior", "lead", "principal",
        "advanced", "intermediate", "basic", "familiar", "beginner", "learning", "developed", "built", "created",
        "implemented", "designed", "architected", "maintained", "deployed", "configured", "administered"
    ));
    
    // Proficiency level indicators with weighted scores
    private static final Map<String, Double> PROFICIENCY_LEVELS = new HashMap<>();
    static {
        PROFICIENCY_LEVELS.put("expert", 1.0);
        PROFICIENCY_LEVELS.put("senior", 0.9);
        PROFICIENCY_LEVELS.put("advanced", 0.85);
        PROFICIENCY_LEVELS.put("proficient", 0.8);
        PROFICIENCY_LEVELS.put("skilled", 0.75);
        PROFICIENCY_LEVELS.put("intermediate", 0.6);
        PROFICIENCY_LEVELS.put("basic", 0.4);
        PROFICIENCY_LEVELS.put("familiar", 0.3);
        PROFICIENCY_LEVELS.put("beginner", 0.2);
        PROFICIENCY_LEVELS.put("learning", 0.1);
    }
    
    // Experience level indicators
    private static final Map<String, Integer> EXPERIENCE_LEVELS = new HashMap<>();
    static {
        EXPERIENCE_LEVELS.put("senior", 5);
        EXPERIENCE_LEVELS.put("lead", 6);
        EXPERIENCE_LEVELS.put("principal", 8);
        EXPERIENCE_LEVELS.put("mid", 3);
        EXPERIENCE_LEVELS.put("intermediate", 3);
        EXPERIENCE_LEVELS.put("junior", 1);
        EXPERIENCE_LEVELS.put("entry", 0);
    }
    
    // Initialize OpenNLP processor
    static {
        initialize();
    }
    
    public static class EnhancedMatchResult {
        private double overallScore;
        private Map<String, Double> skillScores;
        private List<String> matchedSkills;
        private List<String> missingSkills;
        private List<String> recommendations;
        private int experienceMatch;
        private String experienceLevel;
        private Map<String, Integer> skillFrequency;
        private double confidenceScore;
        private List<String> detailedAnalysis;
        
        public EnhancedMatchResult(double overallScore, Map<String, Double> skillScores,
                                 List<String> matchedSkills, List<String> missingSkills,
                                 List<String> recommendations, int experienceMatch, 
                                 String experienceLevel, Map<String, Integer> skillFrequency,
                                 double confidenceScore, List<String> detailedAnalysis) {
            this.overallScore = overallScore;
            this.skillScores = skillScores;
            this.matchedSkills = matchedSkills;
            this.missingSkills = missingSkills;
            this.recommendations = recommendations;
            this.experienceMatch = experienceMatch;
            this.experienceLevel = experienceLevel;
            this.skillFrequency = skillFrequency;
            this.confidenceScore = confidenceScore;
            this.detailedAnalysis = detailedAnalysis;
        }
        
        // Getters
        public double getOverallScore() { return overallScore; }
        public Map<String, Double> getSkillScores() { return skillScores; }
        public List<String> getMatchedSkills() { return matchedSkills; }
        public List<String> getMissingSkills() { return missingSkills; }
        public List<String> getRecommendations() { return recommendations; }
        public int getExperienceMatch() { return experienceMatch; }
        public String getExperienceLevel() { return experienceLevel; }
        public Map<String, Integer> getSkillFrequency() { return skillFrequency; }
        public double getConfidenceScore() { return confidenceScore; }
        public List<String> getDetailedAnalysis() { return detailedAnalysis; }
    }
    
    public static class ProcessedText {
        private List<String> tokens;
        private List<String> sentences;
        private Map<String, Double> skillScores;
        private int experienceYears;
        private String experienceLevel;
        private Map<String, Integer> skillFrequency;
        private List<String> detectedSkills;
        
        public ProcessedText(List<String> tokens, List<String> sentences,
                           Map<String, Double> skillScores, int experienceYears, 
                           String experienceLevel, Map<String, Integer> skillFrequency,
                           List<String> detectedSkills) {
            this.tokens = tokens;
            this.sentences = sentences;
            this.skillScores = skillScores;
            this.experienceYears = experienceYears;
            this.experienceLevel = experienceLevel;
            this.skillFrequency = skillFrequency;
            this.detectedSkills = detectedSkills;
        }
        
        // Getters
        public List<String> getTokens() { return tokens; }
        public List<String> getSentences() { return sentences; }
        public Map<String, Double> getSkillScores() { return skillScores; }
        public int getExperienceYears() { return experienceYears; }
        public String getExperienceLevel() { return experienceLevel; }
        public Map<String, Integer> getSkillFrequency() { return skillFrequency; }
        public List<String> getDetectedSkills() { return detectedSkills; }
    }
    
    public static void initialize() {
        try {
            // Initialize OpenNLP tokenizer
            tokenizer = SimpleTokenizer.INSTANCE;
            System.out.println("Enhanced NLP Processor initialized successfully with OpenNLP tokenizer");
        } catch (Exception e) {
            System.err.println("OpenNLP initialization failed, using fallback processing: " + e.getMessage());
            System.out.println("Enhanced NLP will use fallback tokenization for better compatibility");
            // Continue with fallback processing
        }
    }
    
    public static EnhancedMatchResult analyzeCVAndJobEnhanced(String cvText, String jobDescription) {
        if (cvText == null || jobDescription == null || 
            cvText.trim().isEmpty() || jobDescription.trim().isEmpty()) {
            return createDefaultEnhancedResult();
        }
        
        try {
            // Process CV using enhanced NLP
            ProcessedText cvProcessed = processText(cvText);
            
            // Process job description using enhanced NLP
            ProcessedText jobProcessed = processText(jobDescription);
            
            // Enhanced skill matching with context analysis
            Map<String, Double> skillScores = calculateEnhancedSkillScores(cvProcessed, jobProcessed);
            
            // Calculate experience match
            int experienceMatch = jobProcessed.getExperienceYears() - cvProcessed.getExperienceYears();
            
            // Generate enhanced recommendations
            List<String> recommendations = generateEnhancedRecommendations(cvProcessed, jobProcessed, skillScores);
            
            // Calculate overall score with confidence
            double overallScore = calculateOverallScore(skillScores, cvProcessed.getExperienceYears(), 
                                                      jobProcessed.getExperienceYears());
            
            // Calculate confidence score based on data quality
            double confidenceScore = calculateConfidenceScore(cvProcessed, jobProcessed);
            
            // Generate detailed analysis
            List<String> detailedAnalysis = generateDetailedAnalysis(cvProcessed, jobProcessed, skillScores);
            
            // Separate matched and missing skills
            List<String> matchedSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();
            
            for (Map.Entry<String, Double> entry : skillScores.entrySet()) {
                if (entry.getValue() >= 0.5) {
                    matchedSkills.add(entry.getKey());
                } else {
                    missingSkills.add(entry.getKey());
                }
            }
            
            return new EnhancedMatchResult(overallScore, skillScores, matchedSkills, missingSkills,
                                         recommendations, experienceMatch, cvProcessed.getExperienceLevel(),
                                         cvProcessed.getSkillFrequency(), confidenceScore, detailedAnalysis);
            
        } catch (Exception e) {
            System.err.println("Error in enhanced NLP analysis: " + e.getMessage());
            return createDefaultEnhancedResult();
        }
    }
    
    // Text Processing Methods
    public static ProcessedText processText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ProcessedText(new ArrayList<>(), new ArrayList<>(),
                                   new HashMap<>(), 0, "Unknown", new HashMap<>(), new ArrayList<>());
        }
        
        // Preprocess text
        String preprocessedText = preprocessText(text);
        
        // Tokenize using OpenNLP
        List<String> tokens = tokenize(preprocessedText);
        
        // Extract sentences
        List<String> sentences = extractSentences(text);
        
        // Analyze skills with enhanced context
        Map<String, Double> skillScores = analyzeSkillsWithContext(tokens, sentences);
        
        // Extract experience information
        int experienceYears = extractExperienceYears(text);
        String experienceLevel = determineExperienceLevel(experienceYears);
        
        // Get skill frequency
        Map<String, Integer> skillFrequency = calculateSkillFrequency(tokens, sentences);
        
        // Get detected skills
        List<String> detectedSkills = new ArrayList<>(skillScores.keySet());
        
        return new ProcessedText(tokens, sentences, skillScores, experienceYears, 
                               experienceLevel, skillFrequency, detectedSkills);
    }
    
    private static String preprocessText(String text) {
        // Convert to lowercase
        text = text.toLowerCase();
        
        // Remove HTML tags
        text = text.replaceAll("<[^>]*>", " ");
        
        // Remove URLs
        text = text.replaceAll("https?://\\S+", " ");
        
        // Remove email addresses
        text = text.replaceAll("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", " ");
        
        // Remove phone numbers
        text = text.replaceAll("\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b", " ");
        
        // Remove bullet points and special characters
        text = text.replaceAll("[•·*]", " ");
        
        // Normalize whitespace
        text = text.replaceAll("\\s+", " ");
        
        return text.trim();
    }
    
    private static List<String> tokenize(String text) {
        if (tokenizer != null) {
            try {
                return Arrays.asList(tokenizer.tokenize(text));
            } catch (Exception e) {
                System.err.println("OpenNLP tokenization failed, using fallback: " + e.getMessage());
            }
        }
        
        // Enhanced fallback tokenization
        List<String> tokens = new ArrayList<>();
        String[] words = text.split("\\s+");
        
        for (String word : words) {
            // Clean up the word
            word = word.trim();
            if (!word.isEmpty()) {
                // Handle special cases like "C++", "C#", etc.
                if (word.contains("+") || word.contains("#") || word.contains(".")) {
                    tokens.add(word);
                } else {
                    // Split on common separators but keep the word
                    String[] subWords = word.split("[,;|/\\\\]");
                    for (String subWord : subWords) {
                        subWord = subWord.trim();
                        if (!subWord.isEmpty()) {
                            tokens.add(subWord);
                        }
                    }
                }
            }
        }
        
        return tokens;
    }
    
    private static List<String> extractSentences(String text) {
        // Enhanced sentence extraction
        String[] sentences = text.split("[.!?]+\\s+");
        List<String> result = new ArrayList<>();
        
        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (!sentence.isEmpty()) {
                result.add(sentence);
            }
        }
        
        return result;
    }
    
    private static Map<String, Double> analyzeSkillsWithContext(List<String> tokens, List<String> sentences) {
        Map<String, Double> skillScores = new HashMap<>();
        
        // Get skill categories
        Map<String, List<String>> skillCategories = getSkillCategories();
        
        for (Map.Entry<String, List<String>> category : skillCategories.entrySet()) {
            String mainSkill = category.getKey();
            List<String> synonyms = category.getValue();
            
            double maxScore = 0.0;
            
            for (String synonym : synonyms) {
                double score = calculateSkillScore(tokens, sentences, synonym);
                maxScore = Math.max(maxScore, score);
            }
            
            if (maxScore > 0.0) {
                skillScores.put(mainSkill, maxScore);
            }
        }
        
        return skillScores;
    }
    
    private static double calculateSkillScore(List<String> tokens, List<String> sentences, String skill) {
        String skillLower = skill.toLowerCase();
        double score = 0.0;
        
        // Check token frequency
        int tokenCount = 0;
        for (String token : tokens) {
            if (token.contains(skillLower)) {
                tokenCount++;
            }
        }
        
        if (tokenCount == 0) {
            return 0.0;
        }
        
        // Base score from frequency (capped at 0.6)
        score = Math.min(tokenCount * 0.15, 0.6);
        
        // Analyze context in sentences for higher accuracy
        for (String sentence : sentences) {
            if (sentence.toLowerCase().contains(skillLower)) {
                double contextScore = analyzeSkillContext(sentence, skillLower);
                score = Math.max(score, contextScore);
            }
        }
        
        return Math.min(score, 1.0);
    }
    
    private static double analyzeSkillContext(String sentence, String skill) {
        String sentenceLower = sentence.toLowerCase();
        double score = 0.5; // Default score
        
        // Check for proficiency indicators
        for (Map.Entry<String, Double> level : PROFICIENCY_LEVELS.entrySet()) {
            if (sentenceLower.contains(level.getKey())) {
                score = Math.max(score, level.getValue());
            }
        }
        
        // Check for experience indicators
        for (String indicator : SKILL_INDICATORS) {
            if (sentenceLower.contains(indicator)) {
                score = Math.max(score, 0.7);
            }
        }
        
        // Check for years of experience near the skill
        Pattern yearPattern = Pattern.compile("(\\d+)\\s*(?:years?|yrs?)");
        java.util.regex.Matcher matcher = yearPattern.matcher(sentenceLower);
        if (matcher.find()) {
            try {
                int years = Integer.parseInt(matcher.group(1));
                if (years >= 5) {
                    score = Math.max(score, 0.9);
                } else if (years >= 3) {
                    score = Math.max(score, 0.8);
                } else if (years >= 1) {
                    score = Math.max(score, 0.7);
                }
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }
        
        // Check for action verbs indicating active use
        String[] actionVerbs = {"developed", "built", "created", "implemented", "designed", "architected", 
                               "maintained", "deployed", "configured", "administered", "optimized", "debugged"};
        for (String verb : actionVerbs) {
            if (sentenceLower.contains(verb)) {
                score = Math.max(score, 0.75);
            }
        }
        
        return score;
    }
    
    private static int extractExperienceYears(String text) {
        // Enhanced experience extraction with multiple patterns
        Pattern[] patterns = {
            Pattern.compile("(\\d+)\\s*(?:years?|yrs?)\\s*(?:of\\s*)?(?:experience|exp)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(\\d+)\\+\\s*(?:years?|yrs?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:experience|exp)\\s*(?:of\\s*)?(\\d+)\\s*(?:years?|yrs?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(\\d+)\\s*(?:years?|yrs?)\\s*(?:in\\s*)?(?:development|programming|coding)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(\\d+)\\s*(?:years?|yrs?)\\s*(?:working\\s*with|using)", Pattern.CASE_INSENSITIVE)
        };
        
        int maxYears = 0;
        for (Pattern pattern : patterns) {
            java.util.regex.Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                try {
                    int years = Integer.parseInt(matcher.group(1));
                    maxYears = Math.max(maxYears, years);
                } catch (NumberFormatException e) {
                    // Ignore parsing errors
                }
            }
        }
        
        // Check for experience level indicators
        String textLower = text.toLowerCase();
        for (Map.Entry<String, Integer> level : EXPERIENCE_LEVELS.entrySet()) {
            if (textLower.contains(level.getKey())) {
                maxYears = Math.max(maxYears, level.getValue());
            }
        }
        
        return maxYears;
    }
    
    private static String determineExperienceLevel(int years) {
        if (years >= 8) return "Senior/Lead";
        else if (years >= 5) return "Senior";
        else if (years >= 3) return "Mid-level";
        else if (years >= 1) return "Junior";
        else return "Entry-level";
    }
    
    private static Map<String, Integer> calculateSkillFrequency(List<String> tokens, List<String> sentences) {
        Map<String, Integer> frequency = new HashMap<>();
        Map<String, List<String>> skillCategories = getSkillCategories();
        
        for (Map.Entry<String, List<String>> category : skillCategories.entrySet()) {
            String mainSkill = category.getKey();
            List<String> synonyms = category.getValue();
            
            int totalCount = 0;
            for (String synonym : synonyms) {
                String synonymLower = synonym.toLowerCase();
                
                // Count in tokens
                for (String token : tokens) {
                    if (token.contains(synonymLower)) {
                        totalCount++;
                    }
                }
                
                // Count in sentences
                for (String sentence : sentences) {
                    if (sentence.toLowerCase().contains(synonymLower)) {
                        totalCount++;
                    }
                }
            }
            
            if (totalCount > 0) {
                frequency.put(mainSkill, totalCount);
            }
        }
        
        return frequency;
    }
    
    // Enhanced Matching Methods
    private static Map<String, Double> calculateEnhancedSkillScores(ProcessedText cvProcessed,
                                                                   ProcessedText jobProcessed) {
        Map<String, Double> scores = new HashMap<>();
        Map<String, Double> cvSkills = cvProcessed.getSkillScores();
        Map<String, Double> jobSkills = jobProcessed.getSkillScores();
        
        if (jobSkills.isEmpty()) {
            // If no job skills detected, give partial scores for CV skills
            for (Map.Entry<String, Double> entry : cvSkills.entrySet()) {
                scores.put(entry.getKey(), entry.getValue() * 0.6);
            }
            return scores;
        }
        
        // Calculate enhanced scores for skills mentioned in job requirements
        for (String jobSkill : jobSkills.keySet()) {
            double cvScore = cvSkills.getOrDefault(jobSkill, 0.0);
            double jobImportance = jobSkills.get(jobSkill);
            
            if (cvScore > 0) {
                // Enhanced match score with multiple factors
                double frequencyBonus = calculateFrequencyBonus(cvProcessed, jobSkill);
                double contextBonus = calculateContextBonus(cvProcessed, jobSkill);
                double experienceBonus = calculateExperienceBonus(cvProcessed, jobSkill);
                
                double matchScore = (cvScore * 0.5) + (jobImportance * 0.2) + 
                                   (frequencyBonus * 0.1) + (contextBonus * 0.1) + (experienceBonus * 0.1);
                
                scores.put(jobSkill, Math.min(matchScore, 1.0));
            } else {
                scores.put(jobSkill, 0.0);
            }
        }
        
        return scores;
    }
    
    private static double calculateFrequencyBonus(ProcessedText processed, String skill) {
        Map<String, Integer> frequency = processed.getSkillFrequency();
        int freq = frequency.getOrDefault(skill, 0);
        
        if (freq >= 5) return 0.3;
        else if (freq >= 3) return 0.2;
        else if (freq >= 1) return 0.1;
        else return 0.0;
    }
    
    private static double calculateContextBonus(ProcessedText processed, String skill) {
        List<String> sentences = processed.getSentences();
        double bonus = 0.0;
        
        for (String sentence : sentences) {
            if (sentence.toLowerCase().contains(skill.toLowerCase())) {
                // Check for positive context indicators
                String[] positiveIndicators = {"expert", "senior", "proficient", "skilled", "advanced", 
                                             "developed", "built", "created", "implemented", "designed"};
                
                for (String indicator : positiveIndicators) {
                    if (sentence.toLowerCase().contains(indicator)) {
                        bonus += 0.1;
                    }
                }
            }
        }
        
        return Math.min(bonus, 0.3);
    }
    
    private static double calculateExperienceBonus(ProcessedText processed, String skill) {
        int experienceYears = processed.getExperienceYears();
        
        if (experienceYears >= 5) return 0.3;
        else if (experienceYears >= 3) return 0.2;
        else if (experienceYears >= 1) return 0.1;
        else return 0.0;
    }
    
    private static double calculateOverallScore(Map<String, Double> skillScores, int cvExperience, int jobExperience) {
        if (skillScores.isEmpty()) {
            return 0.1;
        }
        
        // Calculate skill match score
        double skillScore = skillScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        
        // Calculate experience match score
        double experienceScore = 1.0;
        if (jobExperience > 0) {
            if (cvExperience >= jobExperience) {
                experienceScore = 1.0;
            } else if (cvExperience >= jobExperience * 0.8) {
                experienceScore = 0.9;
            } else if (cvExperience >= jobExperience * 0.6) {
                experienceScore = 0.7;
            } else if (cvExperience >= jobExperience * 0.4) {
                experienceScore = 0.5;
            } else {
                experienceScore = 0.3;
            }
        }
        
        // Weighted combination (70% skills, 30% experience)
        double finalScore = (skillScore * 0.7) + (experienceScore * 0.3);
        
        return Math.max(finalScore, 0.1);
    }
    
    private static double calculateConfidenceScore(ProcessedText cvProcessed, ProcessedText jobProcessed) {
        double confidence = 0.5; // Base confidence
        
        // CV quality factors
        int cvTokens = cvProcessed.getTokens().size();
        int cvSentences = cvProcessed.getSentences().size();
        int cvSkills = cvProcessed.getDetectedSkills().size();
        
        if (cvTokens > 100) confidence += 0.1;
        if (cvSentences > 10) confidence += 0.1;
        if (cvSkills > 5) confidence += 0.1;
        
        // Job description quality factors
        int jobTokens = jobProcessed.getTokens().size();
        int jobSentences = jobProcessed.getSentences().size();
        int jobSkills = jobProcessed.getDetectedSkills().size();
        
        if (jobTokens > 50) confidence += 0.1;
        if (jobSentences > 5) confidence += 0.1;
        if (jobSkills > 3) confidence += 0.1;
        
        return Math.min(confidence, 1.0);
    }
    
    private static List<String> generateEnhancedRecommendations(ProcessedText cvProcessed,
                                                              ProcessedText jobProcessed,
                                                              Map<String, Double> skillScores) {
        List<String> recommendations = new ArrayList<>();
        
        // Skill-based recommendations with priority
        List<String> skillsToImprove = new ArrayList<>();
        for (Map.Entry<String, Double> entry : skillScores.entrySet()) {
            if (entry.getValue() < 0.5) {
                skillsToImprove.add(entry.getKey());
            }
        }
        
        if (!skillsToImprove.isEmpty()) {
            if (skillsToImprove.size() <= 3) {
                for (String skill : skillsToImprove) {
                    recommendations.add("Focus on strengthening your " + skill + " skills through projects or courses");
                }
            } else {
                recommendations.add("Prioritize developing these key skills: " +
                        String.join(", ", skillsToImprove.subList(0, 3)) + " and others");
            }
        }
        
        // Experience-based recommendations
        int experienceGap = jobProcessed.getExperienceYears() - cvProcessed.getExperienceYears();
        if (experienceGap > 0) {
            if (experienceGap == 1) {
                recommendations.add("Consider gaining 1 more year of relevant experience through projects or freelance work");
            } else {
                recommendations.add("Build more experience through side projects, open source contributions, or relevant work");
            }
        }
        
        // Context-based recommendations
        if (cvProcessed.getExperienceYears() < 2) {
            recommendations.add("Consider adding more detailed project descriptions to showcase your technical skills");
        }
        
        if (cvProcessed.getDetectedSkills().size() < 5) {
            recommendations.add("Expand your skill set by learning complementary technologies");
        }
        
        return recommendations;
    }
    
    private static List<String> generateDetailedAnalysis(ProcessedText cvProcessed,
                                                        ProcessedText jobProcessed,
                                                        Map<String, Double> skillScores) {
        List<String> analysis = new ArrayList<>();
        
        // CV Analysis
        analysis.add("CV Analysis:");
        analysis.add("- Detected " + cvProcessed.getDetectedSkills().size() + " skills");
        analysis.add("- Experience level: " + cvProcessed.getExperienceLevel());
        analysis.add("- Total tokens processed: " + cvProcessed.getTokens().size());
        analysis.add("- Sentences analyzed: " + cvProcessed.getSentences().size());
        
        // Job Analysis
        analysis.add("\nJob Requirements Analysis:");
        analysis.add("- Required skills: " + jobProcessed.getDetectedSkills().size());
        analysis.add("- Required experience: " + jobProcessed.getExperienceYears() + " years");
        analysis.add("- Experience level: " + jobProcessed.getExperienceLevel());
        
        // Match Analysis
        analysis.add("\nMatch Analysis:");
        int matchedCount = 0;
        for (Double score : skillScores.values()) {
            if (score >= 0.5) matchedCount++;
        }
        analysis.add("- Skills matched: " + matchedCount + "/" + skillScores.size());
        
        // Top skills by score
        analysis.add("- Top matching skills:");
        skillScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> analysis.add("  • " + entry.getKey() + ": " + 
                        String.format("%.1f", entry.getValue() * 100) + "%"));
        
        return analysis;
    }
    
    // Skill Categories
    private static Map<String, List<String>> getSkillCategories() {
        Map<String, List<String>> categories = new HashMap<>();
        
        // Programming Languages
        categories.put("Java", Arrays.asList("java", "j2ee", "j2se", "jee", "java ee", "java se", "spring", "spring boot",
                "spring framework", "spring mvc", "spring security", "spring data", "hibernate", "jpa", "maven",
                "gradle", "ant", "jvm", "openjdk", "oracle jdk", "tomcat", "jetty", "wildfly", "jboss"));
        
        categories.put("Python", Arrays.asList("python", "python3", "py", "django", "django rest framework", "flask", "fastapi",
                "pyramid", "tornado", "pandas", "numpy", "scipy", "matplotlib", "seaborn", "scikit-learn",
                "sklearn", "tensorflow", "keras", "pytorch", "opencv", "pillow", "requests", "beautifulsoup"));
        
        categories.put("JavaScript", Arrays.asList("javascript", "js", "ecmascript", "es6", "es2015", "es2020", "typescript", "ts",
                "node.js", "nodejs", "npm", "yarn", "pnpm", "react", "reactjs", "react native", "angular",
                "angularjs", "vue", "vuejs", "nuxt", "next.js", "express", "expressjs", "koa", "fastify"));
        
        categories.put("C++", Arrays.asList("c++", "cpp", "c plus plus", "stl", "standard template library", "boost", "qt", "cmake",
                "make", "gcc", "clang", "visual studio", "code blocks", "dev c++", "templates", "pointers"));
        
        categories.put("C#", Arrays.asList("c#", "csharp", "c sharp", ".net", "dotnet", ".net core", ".net framework", ".net 5",
                ".net 6", ".net 7", ".net 8", "asp.net", "asp.net core", "asp.net mvc", "asp.net web api",
                "entity framework", "ef core", "linq", "xamarin", "blazor", "winforms", "wpf", "uwp", "maui"));
        
        // Databases
        categories.put("SQL", Arrays.asList("sql", "structured query language", "queries", "joins", "subqueries", "stored procedures",
                "triggers", "views", "indexes", "normalization", "acid", "transactions", "database design"));
        
        categories.put("MySQL", Arrays.asList("mysql", "my sql", "mariadb", "percona", "mysql workbench", "phpmyadmin"));
        
        categories.put("PostgreSQL", Arrays.asList("postgresql", "postgres", "psql", "pgadmin", "postgis", "jsonb"));
        
        categories.put("MongoDB", Arrays.asList("mongodb", "mongo", "nosql", "document database", "mongoose", "compass"));
        
        // Cloud & DevOps
        categories.put("AWS", Arrays.asList("aws", "amazon web services", "ec2", "s3", "lambda", "rds", "dynamodb", "cloudformation",
                "terraform", "iam", "vpc", "route 53", "cloudfront", "api gateway", "sqs", "sns", "ecs", "eks"));
        
        categories.put("Azure", Arrays.asList("azure", "microsoft azure", "azure functions", "app service", "sql database", "cosmos db",
                "blob storage", "service bus", "event hub", "logic apps", "power apps", "power bi"));
        
        categories.put("Docker", Arrays.asList("docker", "containerization", "containers", "dockerfile", "docker-compose",
                "docker swarm", "multi-stage builds", "docker hub", "registry", "volumes", "networks"));
        
        categories.put("Kubernetes", Arrays.asList("kubernetes", "k8s", "kube", "helm", "kubectl", "pods", "services", "deployments",
                "configmaps", "secrets", "ingress", "persistent volumes", "statefulsets", "daemonsets"));
        
        // Web Technologies
        categories.put("HTML", Arrays.asList("html", "html5", "markup", "semantic html", "accessibility", "a11y", "aria"));
        
        categories.put("CSS", Arrays.asList("css", "css3", "cascading style sheets", "flexbox", "grid", "responsive design",
                "media queries", "animations", "transitions", "preprocessors", "sass", "scss", "less", "stylus"));
        
        return categories;
    }
    
    private static EnhancedMatchResult createDefaultEnhancedResult() {
        return new EnhancedMatchResult(0.1, new HashMap<>(), new ArrayList<>(), new ArrayList<>(),
                                     Arrays.asList("Unable to analyze the provided text"), 0, "Unknown",
                                     new HashMap<>(), 0.0, Arrays.asList("Analysis failed"));
    }
} 