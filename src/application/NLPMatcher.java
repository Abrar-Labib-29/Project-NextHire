package application;

import java.util.*;
import java.util.regex.*;

public class NLPMatcher {

    // Enhanced skill categories with synonyms and related terms
    private static final Map<String, List<String>> SKILL_CATEGORIES = new HashMap<>();

    static {
        // Programming Languages
        SKILL_CATEGORIES.put("Java",
                Arrays.asList("java", "j2ee", "j2se", "jee", "java ee", "java se", "spring", "spring boot",
                        "spring framework", "spring mvc", "spring security", "spring data", "hibernate", "jpa", "maven",
                        "gradle", "ant", "jvm", "openjdk", "oracle jdk", "tomcat", "jetty", "wildfly", "jboss",
                        "struts", "jsf", "servlets", "jsp", "jstl"));

        SKILL_CATEGORIES.put("Python",
                Arrays.asList("python", "python3", "py", "django", "django rest framework", "flask", "fastapi",
                        "pyramid", "tornado", "pandas", "numpy", "scipy", "matplotlib", "seaborn", "scikit-learn",
                        "sklearn", "tensorflow", "keras", "pytorch", "opencv", "pillow", "requests", "beautifulsoup",
                        "scrapy", "celery", "jupyter", "anaconda", "conda", "pip", "virtualenv", "poetry", "black",
                        "flake8", "pytest", "unittest"));

        SKILL_CATEGORIES.put("JavaScript",
                Arrays.asList("javascript", "js", "ecmascript", "es6", "es2015", "es2020", "typescript", "ts",
                        "node.js", "nodejs", "npm", "yarn", "pnpm", "react", "reactjs", "react native", "angular",
                        "angularjs", "vue", "vuejs", "nuxt", "next.js", "express", "expressjs", "koa", "fastify",
                        "jquery", "lodash", "axios", "fetch", "webpack", "vite", "parcel", "babel", "eslint",
                        "prettier", "jest", "mocha", "chai", "cypress", "playwright"));

        SKILL_CATEGORIES.put("TypeScript", Arrays.asList("typescript", "ts", "type script", "strongly typed",
                "static typing", "interfaces", "generics", "decorators"));

        SKILL_CATEGORIES.put("C++",
                Arrays.asList("c++", "cpp", "c plus plus", "stl", "standard template library", "boost", "qt", "cmake",
                        "make", "gcc", "clang", "visual studio", "code blocks", "dev c++", "templates", "pointers",
                        "memory management", "object oriented programming", "oop"));

        SKILL_CATEGORIES.put("C#",
                Arrays.asList("c#", "csharp", "c sharp", ".net", "dotnet", ".net core", ".net framework", ".net 5",
                        ".net 6", ".net 7", ".net 8", "asp.net", "asp.net core", "asp.net mvc", "asp.net web api",
                        "entity framework", "ef core", "linq", "xamarin", "blazor", "winforms", "wpf", "uwp", "maui",
                        "nuget", "visual studio", "vs code"));

        SKILL_CATEGORIES.put("PHP",
                Arrays.asList("php", "laravel", "symfony", "codeigniter", "zend", "cakephp", "wordpress", "drupal",
                        "joomla", "composer", "psr", "phpunit", "xdebug", "apache", "nginx", "lamp", "wamp", "xampp"));

        SKILL_CATEGORIES.put("Ruby", Arrays.asList("ruby", "ruby on rails", "rails", "ror", "sinatra", "gem", "bundler",
                "rake", "rspec", "minitest", "capistrano", "sidekiq", "puma", "unicorn"));

        SKILL_CATEGORIES.put("Go", Arrays.asList("go", "golang", "goroutines", "channels", "gin", "echo", "fiber",
                "beego", "revel", "gorm", "go modules", "go mod"));

        SKILL_CATEGORIES.put("Rust", Arrays.asList("rust", "cargo", "rustc", "actix", "rocket", "tokio", "serde",
                "diesel", "wasm", "webassembly"));

        SKILL_CATEGORIES.put("Kotlin", Arrays.asList("kotlin", "android", "spring boot kotlin", "ktor",
                "gradle kotlin dsl", "coroutines", "jetpack compose"));

        SKILL_CATEGORIES.put("Swift", Arrays.asList("swift", "ios", "xcode", "cocoa", "cocoa touch", "objective-c",
                "swiftui", "uikit", "core data", "alamofire"));

        SKILL_CATEGORIES.put("Scala",
                Arrays.asList("scala", "akka", "play framework", "spark", "sbt", "functional programming"));

        SKILL_CATEGORIES.put("R",
                Arrays.asList("r", "rstudio", "shiny", "ggplot2", "dplyr", "tidyr", "cran", "statistical analysis"));

        // Database Technologies
        SKILL_CATEGORIES.put("SQL",
                Arrays.asList("sql", "structured query language", "queries", "joins", "subqueries", "stored procedures",
                        "triggers", "views", "indexes", "normalization", "acid", "transactions", "database design",
                        "er diagram", "relational database", "rdbms"));

        SKILL_CATEGORIES.put("MySQL", Arrays.asList("mysql", "my sql", "mariadb", "percona", "mysql workbench",
                "phpmyadmin", "innodb", "myisam"));

        SKILL_CATEGORIES.put("PostgreSQL",
                Arrays.asList("postgresql", "postgres", "psql", "pgadmin", "postgis", "jsonb", "pl/pgsql"));

        SKILL_CATEGORIES.put("Oracle",
                Arrays.asList("oracle", "oracle db", "oracle database", "pl/sql", "sql developer", "toad", "apex"));

        SKILL_CATEGORIES.put("SQL Server",
                Arrays.asList("sql server", "microsoft sql server", "mssql", "ssms", "t-sql", "ssis", "ssrs", "ssas"));

        SKILL_CATEGORIES.put("MongoDB", Arrays.asList("mongodb", "mongo", "nosql", "document database", "mongoose",
                "compass", "atlas", "aggregation", "bson", "gridfs"));

        SKILL_CATEGORIES.put("Redis",
                Arrays.asList("redis", "in-memory", "cache", "key-value store", "pub/sub", "lua scripting"));

        SKILL_CATEGORIES.put("Elasticsearch", Arrays.asList("elasticsearch", "elastic", "elk stack", "kibana",
                "logstash", "beats", "search engine", "full-text search"));

        SKILL_CATEGORIES.put("Cassandra",
                Arrays.asList("cassandra", "apache cassandra", "cql", "distributed database", "wide column"));

        SKILL_CATEGORIES.put("DynamoDB",
                Arrays.asList("dynamodb", "amazon dynamodb", "aws dynamodb", "serverless database"));

        // Web Technologies & Frontend
        SKILL_CATEGORIES.put("HTML", Arrays.asList("html", "html5", "markup", "semantic html", "accessibility", "a11y",
                "aria", "web standards", "w3c", "dom", "forms", "canvas", "svg"));

        SKILL_CATEGORIES.put("CSS",
                Arrays.asList("css", "css3", "cascading style sheets", "flexbox", "grid", "responsive design",
                        "media queries", "animations", "transitions", "preprocessors", "sass", "scss", "less", "stylus",
                        "postcss", "bootstrap", "tailwind", "bulma", "foundation", "material design",
                        "styled components", "emotion", "css modules"));

        SKILL_CATEGORIES.put("Frontend",
                Arrays.asList("frontend", "front-end", "client-side", "ui", "user interface", "ux", "user experience",
                        "responsive", "mobile-first", "progressive web app", "pwa", "spa", "single page application",
                        "web components", "micro frontends", "accessibility", "cross-browser",
                        "performance optimization", "seo", "lighthouse"));

        SKILL_CATEGORIES.put("Backend",
                Arrays.asList("backend", "back-end", "server-side", "api", "rest", "restful", "graphql", "grpc", "soap",
                        "microservices", "monolith", "serverless", "middleware", "authentication", "authorization",
                        "jwt", "oauth", "session management", "rate limiting", "caching", "load balancing"));

        // JavaScript Frameworks & Libraries
        SKILL_CATEGORIES.put("React",
                Arrays.asList("react", "reactjs", "jsx", "tsx", "hooks", "context", "redux", "mobx", "recoil",
                        "zustand", "react router", "next.js", "gatsby", "create react app", "cra",
                        "react testing library", "enzyme", "storybook", "styled components", "material-ui", "mui",
                        "ant design", "chakra ui"));

        SKILL_CATEGORIES.put("Angular",
                Arrays.asList("angular", "angularjs", "typescript", "rxjs", "ngrx", "angular cli", "angular material",
                        "ionic", "ng-bootstrap", "angular universal", "karma", "jasmine", "protractor"));

        SKILL_CATEGORIES.put("Vue", Arrays.asList("vue", "vuejs", "vue.js", "composition api", "options api", "vuex",
                "pinia", "vue router", "nuxt", "nuxt.js", "quasar", "vuetify", "vue cli", "vite"));

        // Backend Frameworks
        SKILL_CATEGORIES.put("Spring",
                Arrays.asList("spring", "spring boot", "spring framework", "spring mvc", "spring security",
                        "spring data", "spring cloud", "spring batch", "spring integration", "spring webflux",
                        "reactive programming", "actuator", "micrometer"));

        SKILL_CATEGORIES.put("Node.js",
                Arrays.asList("node", "nodejs", "node.js", "npm", "yarn", "pnpm", "express", "koa", "fastify", "hapi",
                        "nest.js", "socket.io", "pm2", "nodemon", "event loop", "streams", "buffers", "modules",
                        "commonjs", "esm"));

        SKILL_CATEGORIES.put("Django", Arrays.asList("django", "django rest framework", "drf", "orm", "models", "views",
                "templates", "middleware", "signals", "admin", "migrations", "celery", "channels", "wagtail"));

        SKILL_CATEGORIES.put("Flask", Arrays.asList("flask", "jinja2", "werkzeug", "sqlalchemy", "flask-restful",
                "flask-login", "flask-wtf", "blueprints", "application factory"));

        // Mobile Development
        SKILL_CATEGORIES.put("Android",
                Arrays.asList("android", "android studio", "kotlin", "java", "xml", "gradle", "adb", "jetpack compose",
                        "retrofit", "okhttp", "room", "dagger", "hilt", "rxjava", "coroutines", "firebase",
                        "google play", "material design"));

        SKILL_CATEGORIES.put("iOS",
                Arrays.asList("ios", "swift", "objective-c", "xcode", "cocoa touch", "uikit", "swiftui", "core data",
                        "core animation", "alamofire", "realm", "carthage", "cocoapods", "app store", "testflight"));

        SKILL_CATEGORIES.put("React Native", Arrays.asList("react native", "expo", "metro", "flipper",
                "react navigation", "native base", "react native elements"));

        SKILL_CATEGORIES.put("Flutter", Arrays.asList("flutter", "dart", "widgets", "bloc", "provider", "riverpod",
                "firebase", "pub.dev", "android studio", "vs code"));

        // Cloud Platforms
        SKILL_CATEGORIES.put("AWS",
                Arrays.asList("aws", "amazon web services", "ec2", "s3", "lambda", "rds", "dynamodb", "cloudformation",
                        "terraform", "iam", "vpc", "route 53", "cloudfront", "api gateway", "sqs", "sns", "ecs", "eks",
                        "fargate", "elastic beanstalk", "cloudwatch", "x-ray", "cognito", "amplify", "sagemaker",
                        "redshift", "kinesis", "glue", "step functions", "eventbridge"));

        SKILL_CATEGORIES.put("Azure",
                Arrays.asList("azure", "microsoft azure", "azure functions", "app service", "sql database", "cosmos db",
                        "blob storage", "service bus", "event hub", "logic apps", "power apps", "power bi",
                        "active directory", "key vault", "kubernetes service", "aks", "devops", "pipelines"));

        SKILL_CATEGORIES.put("Google Cloud",
                Arrays.asList("google cloud", "gcp", "compute engine", "app engine", "cloud functions", "cloud storage",
                        "bigquery", "cloud sql", "firestore", "pub/sub", "cloud run", "gke", "kubernetes engine",
                        "cloud build", "cloud deployment manager"));

        SKILL_CATEGORIES.put("Firebase",
                Arrays.asList("firebase", "firestore", "realtime database", "authentication", "hosting",
                        "cloud functions", "storage", "messaging", "analytics", "crashlytics", "remote config",
                        "ab testing"));

        // DevOps & Tools
        SKILL_CATEGORIES.put("Docker",
                Arrays.asList("docker", "containerization", "containers", "dockerfile", "docker-compose",
                        "docker swarm", "multi-stage builds", "docker hub", "registry", "volumes", "networks"));

        SKILL_CATEGORIES.put("Kubernetes",
                Arrays.asList("kubernetes", "k8s", "pods", "services", "deployments", "ingress", "configmap", "secrets",
                        "helm", "kubectl", "minikube", "kustomize", "istio", "prometheus", "grafana"));

        SKILL_CATEGORIES.put("Git",
                Arrays.asList("git", "github", "gitlab", "bitbucket", "version control", "vcs", "branching", "merging",
                        "pull requests", "merge requests", "code review", "gitflow", "rebase", "cherry-pick", "hooks",
                        "submodules", "github actions", "gitlab ci"));

        SKILL_CATEGORIES.put("CI/CD",
                Arrays.asList("ci/cd", "continuous integration", "continuous deployment", "continuous delivery",
                        "jenkins", "github actions", "gitlab ci", "azure devops", "bamboo", "teamcity", "circleci",
                        "travis ci", "buildkite", "pipeline", "automation", "deployment", "blue-green deployment",
                        "canary deployment"));

        SKILL_CATEGORIES.put("Infrastructure as Code", Arrays.asList("infrastructure as code", "iac", "terraform",
                "ansible", "puppet", "chef", "cloudformation", "arm templates", "pulumi", "vagrant"));

        SKILL_CATEGORIES.put("Monitoring",
                Arrays.asList("monitoring", "observability", "prometheus", "grafana", "elk stack", "splunk", "datadog",
                        "new relic", "appdynamics", "dynatrace", "pingdom", "nagios", "zabbix", "logs", "metrics",
                        "tracing", "apm"));

        // Testing
        SKILL_CATEGORIES.put("Testing",
                Arrays.asList("testing", "unit testing", "integration testing", "e2e testing", "end to end",
                        "functional testing", "performance testing", "load testing", "stress testing",
                        "security testing", "automation testing", "manual testing", "qa", "quality assurance",
                        "test driven development", "tdd", "behavior driven development", "bdd", "junit", "testng",
                        "mockito", "selenium", "cypress", "playwright", "jest", "mocha", "chai", "karma", "jasmine",
                        "pytest", "unittest", "rspec", "cucumber", "postman", "insomnia", "jmeter", "gatling"));

        // Data Science & Analytics
        SKILL_CATEGORIES.put("Data Science",
                Arrays.asList("data science", "machine learning", "ml", "artificial intelligence", "ai",
                        "deep learning", "neural networks", "statistics", "data analysis", "data visualization",
                        "big data", "predictive modeling", "classification", "regression", "clustering", "nlp",
                        "natural language processing", "computer vision", "feature engineering", "model deployment"));

        SKILL_CATEGORIES.put("Machine Learning",
                Arrays.asList("machine learning", "ml", "scikit-learn", "sklearn", "tensorflow", "keras", "pytorch",
                        "xgboost", "lightgbm", "catboost", "random forest", "svm", "linear regression",
                        "logistic regression", "decision trees", "gradient boosting", "ensemble methods",
                        "cross validation", "hyperparameter tuning"));

        SKILL_CATEGORIES.put("Data Analysis",
                Arrays.asList("data analysis", "pandas", "numpy", "matplotlib", "seaborn", "plotly", "tableau",
                        "power bi", "excel", "r", "ggplot2", "dplyr", "statistical analysis", "hypothesis testing",
                        "a/b testing", "cohort analysis", "funnel analysis"));

        SKILL_CATEGORIES.put("Big Data",
                Arrays.asList("big data", "hadoop", "spark", "apache spark", "hive", "pig", "hbase", "kafka", "storm",
                        "flink", "airflow", "luigi", "prefect", "databricks", "snowflake", "redshift", "bigquery",
                        "data lake", "data warehouse", "etl", "elt"));

        // Security
        SKILL_CATEGORIES.put("Security",
                Arrays.asList("security", "cybersecurity", "information security", "application security",
                        "network security", "web security", "penetration testing", "vulnerability assessment", "owasp",
                        "ssl", "tls", "encryption", "authentication", "authorization", "oauth", "saml", "ldap",
                        "active directory", "firewall", "ids", "ips", "siem", "gdpr", "compliance", "pci dss"));

        // Methodologies & Practices
        SKILL_CATEGORIES.put("Agile",
                Arrays.asList("agile", "scrum", "kanban", "sprint", "user stories", "backlog", "retrospective",
                        "daily standup", "sprint planning", "sprint review", "scrum master", "product owner",
                        "velocity", "burndown chart", "jira", "confluence", "trello", "asana"));

        SKILL_CATEGORIES.put("DevOps",
                Arrays.asList("devops", "site reliability engineering", "sre", "infrastructure", "automation",
                        "configuration management", "deployment automation", "monitoring", "logging", "alerting",
                        "incident response", "chaos engineering", "reliability", "scalability", "performance"));

        // Soft Skills & Leadership
        SKILL_CATEGORIES.put("Communication",
                Arrays.asList("communication", "verbal communication", "written communication", "presentation",
                        "public speaking", "documentation", "technical writing", "stakeholder management",
                        "client communication", "cross-functional collaboration", "teamwork", "collaboration",
                        "interpersonal skills", "active listening", "feedback", "conflict resolution"));

        SKILL_CATEGORIES.put("Problem Solving",
                Arrays.asList("problem solving", "analytical thinking", "critical thinking", "troubleshooting",
                        "debugging", "root cause analysis", "creative thinking", "innovation", "research",
                        "investigation", "logical reasoning", "decision making"));

        SKILL_CATEGORIES.put("Leadership",
                Arrays.asList("leadership", "team leadership", "project management", "people management", "mentoring",
                        "coaching", "supervision", "delegation", "motivation", "team building", "strategic thinking",
                        "vision", "influence", "change management", "organizational skills", "time management",
                        "priority management"));

        SKILL_CATEGORIES.put("Project Management",
                Arrays.asList("project management", "pmp", "prince2", "waterfall", "agile project management",
                        "risk management", "resource management", "budget management", "timeline management",
                        "milestone tracking", "gantt charts", "critical path", "stakeholder management",
                        "scope management", "quality management"));

        // Industry Domains
        SKILL_CATEGORIES.put("E-commerce",
                Arrays.asList("e-commerce", "ecommerce", "online retail", "payment processing", "shopping cart",
                        "inventory management", "order management", "stripe", "paypal", "shopify", "magento",
                        "woocommerce", "amazon marketplace"));

        SKILL_CATEGORIES.put("Fintech",
                Arrays.asList("fintech", "financial technology", "banking", "payments", "cryptocurrency", "blockchain",
                        "trading", "investment", "insurance", "lending", "regulatory compliance", "kyc", "aml",
                        "pci compliance"));

        SKILL_CATEGORIES.put("Healthcare",
                Arrays.asList("healthcare", "health tech", "medical software", "ehr", "electronic health records",
                        "hipaa", "fhir", "hl7", "telemedicine", "medical devices", "clinical trials",
                        "pharmaceutical"));

        SKILL_CATEGORIES.put("Gaming",
                Arrays.asList("gaming", "game development", "unity", "unreal engine", "c# unity", "c++ unreal",
                        "mobile games", "web games", "multiplayer", "game design", "3d modeling", "animation",
                        "physics", "ai", "procedural generation"));

        // Additional Technologies
        SKILL_CATEGORIES.put("Blockchain",
                Arrays.asList("blockchain", "cryptocurrency", "bitcoin", "ethereum", "smart contracts", "solidity",
                        "web3", "defi", "nft", "distributed ledger", "consensus algorithms", "mining", "staking"));

        SKILL_CATEGORIES.put("IoT",
                Arrays.asList("iot", "internet of things", "embedded systems", "arduino", "raspberry pi", "sensors",
                        "mqtt", "edge computing", "industrial iot", "home automation", "smart devices"));

        SKILL_CATEGORIES.put("AR/VR", Arrays.asList("ar", "vr", "augmented reality", "virtual reality", "mixed reality",
                "mr", "unity ar", "arcore", "arkit", "oculus", "hololens", "webxr", "three.js", "a-frame"));
    }

    public static class MatchResult {
        private double overallScore;
        private Map<String, Double> skillScores;
        private List<String> matchedSkills;
        private List<String> missingSkills;
        private List<String> recommendations;
        private int experienceMatch;
        private String experienceLevel;

        public MatchResult(double overallScore, Map<String, Double> skillScores,
                List<String> matchedSkills, List<String> missingSkills,
                List<String> recommendations, int experienceMatch, String experienceLevel) {
            this.overallScore = overallScore;
            this.skillScores = skillScores;
            this.matchedSkills = matchedSkills;
            this.missingSkills = missingSkills;
            this.recommendations = recommendations;
            this.experienceMatch = experienceMatch;
            this.experienceLevel = experienceLevel;
        }

        // Getters
        public double getOverallScore() {
            return overallScore;
        }

        public Map<String, Double> getSkillScores() {
            return skillScores;
        }

        public List<String> getMatchedSkills() {
            return matchedSkills;
        }

        public List<String> getMissingSkills() {
            return missingSkills;
        }

        public List<String> getRecommendations() {
            return recommendations;
        }

        public int getExperienceMatch() {
            return experienceMatch;
        }

        public String getExperienceLevel() {
            return experienceLevel;
        }
    }

    public static MatchResult analyzeCVAndJob(String cvText, String jobDescription) {
        try {
            // Add debug logging
            System.out.println("=== CV Analysis Debug ===");
            System.out.println("CV Text length: " + (cvText != null ? cvText.length() : 0));
            System.out.println("Job Description length: " + (jobDescription != null ? jobDescription.length() : 0));
    
            if (cvText == null || cvText.trim().isEmpty()) {
                System.out.println("CV text is null or empty!");
                return createDefaultResult();
            }
    
            if (jobDescription == null || jobDescription.trim().isEmpty()) {
                System.out.println("Job description is null or empty!");
                return createDefaultResult();
            }
    
            // Preprocess text
            String cleanCV = preprocessText(cvText);
            String cleanJob = preprocessText(jobDescription);
    
            System.out.println("Clean CV: " + cleanCV.substring(0, Math.min(200, cleanCV.length())) + "...");
            System.out.println("Clean Job: " + cleanJob.substring(0, Math.min(200, cleanJob.length())) + "...");
    
            // Extract skills using enhanced matching
            Map<String, Double> cvSkills = extractSkillsWithContext(cleanCV);
            Map<String, Double> jobSkills = extractSkillsWithContext(cleanJob);
    
            System.out.println("CV Skills found: " + cvSkills);
            System.out.println("Job Skills found: " + jobSkills);
    
            // If no skills found, use fallback method
            if (cvSkills.isEmpty()) {
                cvSkills = extractSkillsFallback(cleanCV);
                System.out.println("Fallback CV Skills: " + cvSkills);
            }
    
            if (jobSkills.isEmpty()) {
                jobSkills = extractSkillsFallback(cleanJob);
                System.out.println("Fallback Job Skills: " + jobSkills);
            }
    
            // Analyze experience levels
            int cvExperience = extractExperienceYears(cleanCV);
            int jobExperience = extractExperienceYears(cleanJob);
            String experienceLevel = determineExperienceLevel(cvExperience);
    
            System.out.println("CV Experience: " + cvExperience + " years");
            System.out.println("Job Experience: " + jobExperience + " years");
    
            // Calculate skill matching scores
            Map<String, Double> skillScores = calculateSkillScores(cvSkills, jobSkills);
            System.out.println("Skill Scores: " + skillScores);
    
            // Check for missing critical job skills (job weight ≥ 0.5)
            List<String> criticalMissingSkills = new ArrayList<>();
            for (Map.Entry<String, Double> jobSkill : jobSkills.entrySet()) {
                if (jobSkill.getValue() >= 0.5 && !cvSkills.containsKey(jobSkill.getKey())) {
                    criticalMissingSkills.add(jobSkill.getKey());
                }
            }
            System.out.println("Critical Missing Skills: " + criticalMissingSkills);
    
            // Generate recommendations
            List<String> recommendations = generateRecommendations(cvSkills, jobSkills, cvExperience, jobExperience);
    
            // Calculate overall score
            double overallScore = calculateOverallScore(skillScores, cvExperience, jobExperience);
    
            // Cap score if critical skills are missing
            double adjustedScore = (!criticalMissingSkills.isEmpty()) ? Math.min(overallScore, 0.3) : overallScore;
    
            System.out.println("Raw Score: " + overallScore);
            System.out.println("Adjusted Score: " + adjustedScore);
    
            // Separate matched and missing skills
            List<String> matchedSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();
    
            for (Map.Entry<String, Double> entry : skillScores.entrySet()) {
                if (entry.getValue() >= 0.3) {
                    matchedSkills.add(entry.getKey());
                } else {
                    missingSkills.add(entry.getKey());
                }
            }
    
            // Add all job skills to missing if not matched
            for (String jobSkill : jobSkills.keySet()) {
                if (!skillScores.containsKey(jobSkill) || skillScores.get(jobSkill) < 0.3) {
                    if (!missingSkills.contains(jobSkill)) {
                        missingSkills.add(jobSkill);
                    }
                }
            }
    
            System.out.println("Matched Skills: " + matchedSkills);
            System.out.println("Missing Skills: " + missingSkills);
    
            return new MatchResult(adjustedScore, skillScores, matchedSkills, missingSkills,
                    recommendations, jobExperience - cvExperience, experienceLevel);
    
        } catch (Exception e) {
            System.err.println("Error in CV analysis: " + e.getMessage());
            e.printStackTrace();
            return createDefaultResult();
        }
    }
    

    private static MatchResult createDefaultResult() {
        List<String> defaultRecommendations = Arrays.asList(
                "Please ensure your CV contains relevant technical skills",
                "Add more details about your experience and projects",
                "Include specific technologies and frameworks you've worked with");

        return new MatchResult(0.0, new HashMap<>(), new ArrayList<>(),
                Arrays.asList("Unable to analyze skills"),
                defaultRecommendations, 0, "Unknown");
    }

    private static String preprocessText(String text) {
        if (text == null)
            return "";

        // Convert to lowercase
        text = text.toLowerCase();

        // Remove HTML tags if present
        text = text.replaceAll("<[^>]*>", " ");

        // Replace common separators with spaces
        text = text.replaceAll("[,;|/\\\\]", " ");

        // Keep alphanumeric, spaces, and important symbols
        text = text.replaceAll("[^a-z0-9\\s\\+\\#\\.\\-]", " ");

        // Remove extra whitespace
        text = text.replaceAll("\\s+", " ");

        // Normalize common abbreviations
        text = text.replaceAll("\\bjs\\b", "javascript");
        text = text.replaceAll("\\bcpp\\b", "c++");
        text = text.replaceAll("\\bcsharp\\b", "c#");
        text = text.replaceAll("\\bdotnet\\b", ".net");
        text = text.replaceAll("\\bnode\\b", "nodejs");

        return text.trim();
    }

    private static Map<String, Double> extractSkillsWithContext(String text) {
        Map<String, Double> skills = new HashMap<>();

        for (Map.Entry<String, List<String>> category : SKILL_CATEGORIES.entrySet()) {
            String mainSkill = category.getKey();
            List<String> synonyms = category.getValue();

            double maxScore = 0.0;
            for (String synonym : synonyms) {
                double score = calculateSkillContextScore(text, synonym);
                maxScore = Math.max(maxScore, score);
            }

            if (maxScore > 0.0) { // Accept any match
                skills.put(mainSkill, maxScore);
            }
        }

        return skills;
    }

    private static Map<String, Double> extractSkillsFallback(String text) {
        Map<String, Double> skills = new HashMap<>();

        // Simple word matching as fallback
        for (Map.Entry<String, List<String>> category : SKILL_CATEGORIES.entrySet()) {
            String mainSkill = category.getKey();
            List<String> synonyms = category.getValue();

            for (String synonym : synonyms) {
                if (text.contains(synonym.toLowerCase())) {
                    skills.put(mainSkill, 0.7); // Default score
                    break;
                }
            }
        }

        return skills;
    }

    private static double calculateSkillContextScore(String text, String skill) {
        if (!text.contains(skill.toLowerCase())) {
            return 0.0;
        }

        // Count occurrences
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(skill.toLowerCase(), index)) != -1) {
            count++;
            index += skill.length();
        }

        // Base score from occurrence count
        double score = Math.min(count * 0.3, 1.0);

        // Check context around the skill
        String[] sentences = text.split("[.!?\\n]");
        for (String sentence : sentences) {
            if (sentence.contains(skill.toLowerCase())) {
                // Boost score based on context
                if (sentence.contains("experience") || sentence.contains("years") ||
                        sentence.contains("proficient") || sentence.contains("expert") ||
                        sentence.contains("skilled") || sentence.contains("advanced")) {
                    score = Math.max(score, 0.9);
                } else if (sentence.contains("knowledge") || sentence.contains("familiar") ||
                        sentence.contains("basic") || sentence.contains("learning") ||
                        sentence.contains("beginner")) {
                    score = Math.max(score, 0.5);
                } else {
                    score = Math.max(score, 0.7); // Default score for mentions
                }
            }
        }

        return Math.min(score, 1.0);
    }

    private static int extractExperienceYears(String text) {
        // Extract years of experience using regex patterns
        Pattern[] patterns = {
                Pattern.compile("(\\d+)\\s*(?:years?|yrs?)\\s*(?:of\\s*)?(?:experience|exp)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("(\\d+)\\+\\s*(?:years?|yrs?)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("(?:experience|exp)\\s*(?:of\\s*)?(\\d+)\\s*(?:years?|yrs?)", Pattern.CASE_INSENSITIVE)
        };

        int maxYears = 0;
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                try {
                    int years = Integer.parseInt(matcher.group(1));
                    maxYears = Math.max(maxYears, years);
                } catch (NumberFormatException e) {
                    // Ignore parsing errors
                }
            }
        }

        // Also check for experience level indicators
        if (text.contains("senior") || text.contains("lead") || text.contains("principal")) {
            maxYears = Math.max(maxYears, 5);
        } else if (text.contains("mid") || text.contains("intermediate")) {
            maxYears = Math.max(maxYears, 3);
        } else if (text.contains("junior") || text.contains("entry")) {
            maxYears = Math.max(maxYears, 1);
        }

        return maxYears;
    }

    private static String determineExperienceLevel(int years) {
        if (years >= 8)
            return "Senior/Lead";
        else if (years >= 5)
            return "Senior";
        else if (years >= 3)
            return "Mid-level";
        else if (years >= 1)
            return "Junior";
        else
            return "Entry-level";
    }

    private static Map<String, Double> calculateSkillScores(Map<String, Double> cvSkills,
            Map<String, Double> jobSkills) {
        Map<String, Double> scores = new HashMap<>();

        if (jobSkills.isEmpty()) {
            // If no job skills detected, give partial scores for CV skills
            for (Map.Entry<String, Double> entry : cvSkills.entrySet()) {
                scores.put(entry.getKey(), entry.getValue() * 0.8); // Partial match
            }
            return scores;
        }

        // Calculate scores for skills mentioned in job requirements
        for (String jobSkill : jobSkills.keySet()) {
            double cvScore = cvSkills.getOrDefault(jobSkill, 0.0);
            double jobImportance = jobSkills.get(jobSkill);

            if (cvScore > 0) {
                // Calculate match score with importance weighting
                double matchScore = (cvScore * 0.7) + (jobImportance * 0.3);
                scores.put(jobSkill, Math.min(matchScore, 1.0));
            } else {
                scores.put(jobSkill, 0.0);
            }
        }

        return scores;
    }

    private static double calculateOverallScore(Map<String, Double> skillScores, int cvExperience, int jobExperience) {
        if (skillScores.isEmpty()) {
            return 0.1; // Minimum score instead of 0
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
            } else if (cvExperience >= jobExperience * 0.7) {
                experienceScore = 0.8;
            } else if (cvExperience >= jobExperience * 0.5) {
                experienceScore = 0.6;
            } else {
                experienceScore = 0.4; // Minimum experience score
            }
        }

        // Weighted combination (80% skills, 20% experience)
        double finalScore = (skillScore * 0.8) + (experienceScore * 0.2);

        // Ensure minimum score
        return Math.max(finalScore, 0.1);
    }

    private static List<String> generateRecommendations(Map<String, Double> cvSkills,
            Map<String, Double> jobSkills,
            int cvExperience, int jobExperience) {
        List<String> recommendations = new ArrayList<>();

        // Skill-based recommendations
        List<String> skillsToImprove = new ArrayList<>();
        for (String jobSkill : jobSkills.keySet()) {
            double cvSkillLevel = cvSkills.getOrDefault(jobSkill, 0.0);
            if (cvSkillLevel < 0.5) {
                skillsToImprove.add(jobSkill);
            }
        }

        if (!skillsToImprove.isEmpty()) {
            if (skillsToImprove.size() <= 3) {
                for (String skill : skillsToImprove) {
                    recommendations.add("Consider strengthening your " + skill + " skills");
                }
            } else {
                recommendations.add("Focus on developing these key skills: " +
                        String.join(", ", skillsToImprove.subList(0, 3)) + " and others");
            }
        }

        // Experience-based recommendations
        if (cvExperience < jobExperience) {
            int gap = jobExperience - cvExperience;
            if (gap == 1) {
                recommendations.add("Consider gaining 1 more year of relevant experience");
            } else {
                recommendations.add("Build more experience through projects and work in relevant technologies");
            }
        }

        // General recommendations based on overall match
        if (cvSkills.size() < jobSkills.size() * 0.6) {
            recommendations.add("Expand your technical skill set to better match job requirements");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Great match! Your profile aligns well with this position");
            recommendations.add("Consider highlighting your relevant experience in your application");
        }

        return recommendations;
    }
}