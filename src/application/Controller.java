package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URLEncoder;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.Arrays;
import javafx.scene.Cursor;
import java.util.Map;
import java.util.HashMap;

public class Controller implements Initializable {

	private Stage stage;
	private Scene scene;
	private static String usernameForPasswordReset;
	private static boolean isLoggedIn = false;
	private static String loggedInUser = null;
	private static Job selectedAdzunaJob;
	private static double lastMatchPercent = 0.0;
	private static java.util.List<String> lastMatchedSkills = new java.util.ArrayList<>();
	private static java.util.List<String> lastNotMatchedSkills = new java.util.ArrayList<>();
	private static String lastMatchedJobTitle = "";
	private static String lastMatchedJobCompany = "";
	private static String lastMatchedJobLocation = "";
	private static String lastMatchedJobSalary = "";
	
	// New NLP-based matching variables
	private static NLPMatcher.MatchResult lastMatchResult = null;
	private static java.util.List<String> lastRecommendations = new java.util.ArrayList<>();
	private static String lastExperienceLevel = "";
	private static int lastExperienceGap = 0;

	// Enhanced NLP result storage
	private static EnhancedNLPMatcher.EnhancedMatchResult lastEnhancedMatchResult = null;
	private static double lastConfidenceScore = 0.0;
	private static List<String> lastDetailedAnalysis = new ArrayList<>();
	private static Map<String, Integer> lastSkillFrequency = new HashMap<>();

	@FXML
	private HBox loginSignUpBtnContainer;

	@FXML
	private MenuButton profileButton;

	@FXML
	private PasswordField login_password;

	@FXML
	private TextField login_showpassword;

	@FXML
	private CheckBox login_selectshowPassword;

	@FXML
	private ComboBox<String> forgot_selectQuestion;

	@FXML
	private TextField forgot_answer;

	@FXML
	private TextField forgot_username;

	@FXML
	private PasswordField changePass_password;

	@FXML
	private PasswordField changePass_cPassword;

	@FXML
	private TextField signup_email;

	@FXML
	private TextField signup_username;

	@FXML
	private PasswordField signup_password;

	@FXML
	private PasswordField signup_cPassword;

	@FXML
	private ComboBox<String> signup_selectQuestion;

	@FXML
	private TextField signup_answer;

	@FXML
	private TextField login_username;

	@FXML
	private Label selectedFileLabel;
	
	@FXML
	private Label errorLabel;
	private File selectedFile;

	@FXML
	private VBox jobListContainer;

	@FXML
	private Label adzunaJobTitleLabel;
	@FXML
	private Label adzunaJobTitleLabel2;
	@FXML
	private Label adzunaJobCompanyLabel;
	@FXML
	private Label adzunaJobCompanyLabel2;
	@FXML
	private Label adzunaJobLocationLabel;
	@FXML
	private Label adzunaJobLocationLabel2;
	@FXML
	private Label adzunaJobSalaryLabel;
	@FXML
	private Label adzunaJobSalaryLabel2;
	@FXML
	private Label adzunaJobDescriptionContentLabel;

	@FXML
	private Button adzunaSeeMoreBtn;

	@FXML
	private Label adzunaJobResponsibilitiesLabel;
	@FXML
	private Label adzunaJobResponsibilitiesContentLabel;
	@FXML
	private Label adzunaJobQualificationsLabel;
	@FXML
	private Label adzunaJobQualificationsContentLabel;

	@FXML
	private TextField jobSearchField;
	@FXML
	private Button jobSearchBtn;

	@FXML
	private Label matchJobTitleLabel;
	@FXML
	private Label matchJobCompanyLabel;
	@FXML
	private Label matchJobLocationLabel;
	@FXML
	private Label matchJobSalaryLabel;
	@FXML
	private Label matchResultLabel;
	@FXML
	private javafx.scene.control.ProgressIndicator matchProgressIndicator;
	@FXML
	private VBox matchedSkillsBox;
	@FXML
	private VBox notMatchedSkillsBox;
	@FXML
	private Label experienceLevelLabel;
	@FXML
	private Label experienceGapLabel;
	@FXML
	private VBox recommendationsBox;

	@FXML
	private Label matchPercentageLabel;

	@FXML
	private Button applyForJobBtn;

	@FXML
	private Button enrollInCourseBtn;

	@FXML
	private Label enrollmentWarningLabel;

	@FXML
	private AnchorPane enrollmentWarningContainer;

	// Navigation Labels
	@FXML
	private Label homeLabel;

	@FXML
	private Label jobLabel;

	@FXML
	private Label courseLabel;

	@FXML
	private Button watchVideoBtn;

	@FXML
	private Button chatButton;
	
	@FXML
	private MenuItem coursesMenuItem;

	// scene of home page
	public void homePage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of login page
	public void loginPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
		Stage stage;
		if (e.getSource() instanceof Node) {
			stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		} else if (profileButton != null && profileButton.getScene() != null) {
			stage = (Stage) profileButton.getScene().getWindow();
		} else {
			// fallback: get any window
			stage = (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
		}
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of sign up page
	public void signUpPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/SignUp.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of forget pass page
	public void forgetPassPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/ForgetPass.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of forget confirm pass page
	public void forgetConfirmPassPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/ForgetConfirmPass.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of job dashboard page
	public void jobDashboardPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/JobDashBoard.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of job description page
	public void jobDescriptionPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/JobDescription.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of match criteria result page
	public void matchCriteriaResultPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/MatchCriteriaResult.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		
		// The static variables should be available to the new Controller instance
		// The initialize method will handle populating the results
		System.out.println("📄 Navigated to results page");
		System.out.println("- Enhanced Result available: " + (lastEnhancedMatchResult != null));
		System.out.println("- Basic Result available: " + (lastMatchResult != null));
		System.out.println("- Match Percent: " + (lastMatchPercent * 100) + "%");
	}

	// scene of cv upload page
	public void cvUploadPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/CVUpload.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of job dashboard adzuna page
	public void jobDashboardAdzunaPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/JobDashBoardAdzuna.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// scene of job description adzuna page
	public void jobDescriptionAdzunaPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/JobDescriptionAdzuna.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}

	// Scene of Course navigation
	public void coursePage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Course.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void courseSinglePage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/CourseSingle.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public void courseVideoPage(ActionEvent e) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/CourseVideo.fxml"));
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	// home page
	public void getStartedBtn(ActionEvent e) throws IOException {
		if (isLoggedIn) {
			//jobDashboardPage(e);
			jobDashboardAdzunaPage(e);
		} else {
			loginPage(e);
		}
	}

	public void loginBtn(ActionEvent e) throws IOException {
		loginPage(e);
	}

	public void signUpBtn(ActionEvent e) throws IOException {
		signUpPage(e);
	}

	// sign up page - sign up button functionality with database
	public void signUpBtnFunctionality(ActionEvent event) throws IOException {
		if (signup_email.getText().isEmpty() || signup_username.getText().isEmpty() ||
				signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty() ||
				signup_selectQuestion.getSelectionModel().isEmpty() || signup_answer.getText().isEmpty()) {

			showAlert(Alert.AlertType.ERROR, "Error", "Please fill all blank fields");
			return;
		}

		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		if (!signup_email.getText().matches(emailRegex)) {
			showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
			return;
		}

		if (!signup_password.getText().equals(signup_cPassword.getText())) {
			showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
			return;
		}

		// Database logic starts here
		String checkUserSql = "SELECT * FROM users WHERE email = ? OR username = ?";
		String insertSql = "INSERT INTO users (email, username, password, question, answer) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = Database.getConnection();
				PreparedStatement checkStmt = conn.prepareStatement(checkUserSql);
				PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

			// Check if user already exists
			checkStmt.setString(1, signup_email.getText());
			checkStmt.setString(2, signup_username.getText());
			ResultSet rs = checkStmt.executeQuery();

			if (rs.next()) {
				showAlert(Alert.AlertType.ERROR, "Error", "Username or Email already exists.");
				return;
			}

			// Insert new user
			insertStmt.setString(1, signup_email.getText());
			insertStmt.setString(2, signup_username.getText());
			insertStmt.setString(3, signup_password.getText()); // In a real app, you should HASH the password
			insertStmt.setString(4, signup_selectQuestion.getSelectionModel().getSelectedItem());
			insertStmt.setString(5, signup_answer.getText());

			insertStmt.executeUpdate();

			showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
			loginPage(event);

		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while creating your account.");
		}
	}

	// login page
	public void forgetPassBtn(ActionEvent e) throws IOException {
		forgetPassPage(e);
	}

	// login button functionality with database
	public void loginFunctionality(ActionEvent event) throws IOException {
		if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Error", "Please fill all blank fields");
			return;
		}

		String sql = "SELECT * FROM users WHERE username = ?";

		try (Connection conn = Database.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, login_username.getText());

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// User found, now check password
				String dbPassword = rs.getString("password");
				if (dbPassword.equals(login_password.getText())) {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully logged in!");
					isLoggedIn = true;
					loggedInUser = rs.getString("username");
					homePage(event);
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", "Incorrect Password");
				}
			} else {
				// User not found
				showAlert(Alert.AlertType.ERROR, "Error", "Username does not exist");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred during login.");
		}
	}

	// logout button functionality
	public void logoutBtn(ActionEvent e) {
		isLoggedIn = false;
		loggedInUser = null;
		showAlert(Alert.AlertType.INFORMATION, "Success", "You have successfully logged out.");
		updateHomeUI();
		try {
			loginPage(e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// forget pass page
	public void proceedBtn(ActionEvent e) throws IOException {
		String username = forgot_username.getText().trim();
		String question = forgot_selectQuestion.getSelectionModel().getSelectedItem();
		String answer = forgot_answer.getText().trim();

		if (username.isEmpty() || question == null || answer.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields.");
			return;
		}

		String sql = "SELECT * FROM users WHERE username = ? AND question = ? AND answer = ?";

		try (Connection conn = Database.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, question);
			pstmt.setString(3, answer);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// Credentials are correct
				usernameForPasswordReset = username; // Store username for the next step
				forgetConfirmPassPage(e);
			} else {
				showAlert(Alert.AlertType.ERROR, "Verification Failed",
						"The provided information is incorrect. Please try again.");
				usernameForPasswordReset = null; // Clear any previously stored username
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while verifying your information.");
		}
	}

	// forget confirm pass page - change password functionality
	public void changePasswordBtn(ActionEvent e) throws IOException {
		String newPassword = changePass_password.getText();
		String confirmPassword = changePass_cPassword.getText();

		if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Error Message", "Please fill all blank fields");
			return;
		} else if (!newPassword.equals(confirmPassword)) {
			showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
			return;
		}

		// Check if we have a user to update password for
		if (usernameForPasswordReset == null || usernameForPasswordReset.isEmpty()) {
			showAlert(Alert.AlertType.ERROR, "Error",
					"Something went wrong. Please go back and verify your identity again.");
			loginPage(e); // Go back to login, as the state is lost
			return;
		}

		String sql = "UPDATE users SET password = ? WHERE username = ?";

		try (Connection conn = Database.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, newPassword); // In a real app, you should HASH this
			pstmt.setString(2, usernameForPasswordReset);

			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
				usernameForPasswordReset = null; // Clear the stored username
				loginPage(e);
			} else {
				showAlert(Alert.AlertType.ERROR, "Error", "Could not update password. User not found.");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while changing your password.");
		}
	}

	// job dashboard page
	public void seeDetailsBtn(ActionEvent e, Job job) throws IOException {
		//jobDescriptionPage(e);
		selectedAdzunaJob = job;
		lastMatchResult = null;
		jobDescriptionAdzunaPage(e);
	}

	// job description page
	public void handleBackBtn(ActionEvent e) throws IOException {
		//jobDashboardPage(e);
		jobDashboardAdzunaPage(e);
	}

	@FXML
	private void handleCheckEligibilityBtn(ActionEvent e) throws IOException {
		cvUploadPage(e);
	}

	@FXML
	private void handleBackToJobsBtn(ActionEvent e) throws IOException {
		jobDashboardAdzunaPage(e);
	}

	// update home UI based on login status
	private void updateHomeUI() {
		if (profileButton != null) {
			if (isLoggedIn) {
				// Use dynamic sizing for the profile button
				setUsernameWithEllipsis(loggedInUser, 15); // Limit to 15 characters with ellipsis
			} else {
				profileButton.setVisible(false);
				profileButton.setManaged(false);
			}
		}
		if (loginSignUpBtnContainer != null) {
			if (isLoggedIn) {
				loginSignUpBtnContainer.setVisible(false);
				loginSignUpBtnContainer.setManaged(false);
			} else {
				loginSignUpBtnContainer.setVisible(true);
				loginSignUpBtnContainer.setManaged(true);
			}
		}
	}

	// show alert functionality (customized)
	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// You can replace "logo.png" with your own icons for success and error
		stage.getIcons().add(new Image(getClass().getResource("/logo.png").toExternalForm()));

		alert.showAndWait();
	}

	// load jobs from adzuna
	public void loadJobsFromAdzuna(String searchTerm) {
		List<Job> jobs = new ArrayList<>();
		String appId = "e401c888";
		String appKey = "c01f20adfbb0b8ab63c037a69daf8add";
		String country = "us";
		String what = (searchTerm == null || searchTerm.isEmpty()) ? "java developer" : searchTerm;
		String urlString;
		try {
			urlString = String.format(
					"https://api.adzuna.com/v1/api/jobs/%s/search/1?app_id=%s&app_key=%s&what=%s&results_per_page=5",
					country, appId, appKey, URLEncoder.encode(what, "UTF-8")
			);
		} catch (java.io.UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			conn.disconnect();
			JSONObject json = new JSONObject(content.toString());
			JSONArray results = json.getJSONArray("results");
			for (int i = 0; i < results.length(); i++) {
				JSONObject job = results.getJSONObject(i);
				String title = job.optString("title", "N/A");
				String company = job.has("company") ? job.getJSONObject("company").optString("display_name", "N/A") : "N/A";
				String location = job.has("location") ? job.getJSONObject("location").optString("display_name", "N/A") : "N/A";
				String description = job.optString("description", "N/A");
				String urlJob = job.optString("redirect_url", "N/A");
				double salaryMin = job.optDouble("salary_min", 0);
				double salaryMax = job.optDouble("salary_max", 0);
				jobs.add(new Job(title, company, location, description, urlJob, salaryMin, salaryMax));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Populate VBox
		if (jobListContainer != null) {
			jobListContainer.getChildren().clear();
			for (Job job : jobs) {
				AnchorPane jobPane = new AnchorPane();
				jobPane.setPrefHeight(150);
				jobPane.setPrefWidth(200);
				jobPane.getStyleClass().add("formB");
				// Title
				javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(job.getTitle());
				titleLabel.getStyleClass().add("headline2");
				titleLabel.setLayoutX(20);
				titleLabel.setLayoutY(20);
				// Company
				javafx.scene.control.Label companyLabel = new javafx.scene.control.Label(job.getCompany());
				companyLabel.getStyleClass().add("textt");
				companyLabel.setLayoutX(20);
				companyLabel.setLayoutY(50);
				// Location
				javafx.scene.control.Label locationLabel = new javafx.scene.control.Label(job.getLocation());
				locationLabel.getStyleClass().add("textt2");
				locationLabel.setLayoutX(20);
				locationLabel.setLayoutY(80);
				// Salary
				javafx.scene.control.Label salaryLabel = new javafx.scene.control.Label("Salary: $" + job.getSalaryMin() + " - $" + job.getSalaryMax());
				salaryLabel.getStyleClass().add("textt");
				salaryLabel.setLayoutX(20);
				salaryLabel.setLayoutY(110);
				// Details Button
				Button detailsBtn = new Button("See Details");
				detailsBtn.getStyleClass().add("loginBTN");
				detailsBtn.setLayoutX(750);
				detailsBtn.setLayoutY(55);
				detailsBtn.setPrefWidth(120);
				detailsBtn.setPrefHeight(40);
				//String jobUrl = job.getUrl();
				detailsBtn.setOnAction(e -> {
					try {
						seeDetailsBtn(e, job);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
				jobPane.getChildren().addAll(titleLabel, companyLabel, locationLabel, salaryLabel, detailsBtn);
				jobListContainer.getChildren().add(jobPane);
			}
		}
	}

	// analyze cv and match using NLP
	private void analyzeCVAndMatch(File cvFile, Job job) {
		try {
			System.out.println("=== Starting CV Analysis ===");
			System.out.println("CV File: " + cvFile.getName());
			System.out.println("Job Title: " + job.getTitle());
			
			// Load and extract text from PDF
			PDDocument document = Loader.loadPDF(cvFile);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String cvText = pdfStripper.getText(document);
			document.close();
			
			System.out.println("CV Text extracted, length: " + cvText.length());
			
			// Get job description
			String jobDescription = job.getDescription();
			System.out.println("Job description length: " + jobDescription.length());
			
			// Perform Enhanced NLP analysis
			EnhancedNLPMatcher.EnhancedMatchResult enhancedResult = EnhancedNLPMatcher.analyzeCVAndJobEnhanced(cvText, jobDescription);
			
			// Store enhanced results with validation
			if (enhancedResult != null) {
				lastEnhancedMatchResult = enhancedResult;
				lastMatchResult = null; // Clear old result
				lastMatchPercent = Math.max(enhancedResult.getOverallScore(), 0.1); // Ensure minimum 10%
				lastMatchedSkills = enhancedResult.getMatchedSkills() != null ? enhancedResult.getMatchedSkills() : new ArrayList<>();
				lastNotMatchedSkills = enhancedResult.getMissingSkills() != null ? enhancedResult.getMissingSkills() : new ArrayList<>();
				lastRecommendations = enhancedResult.getRecommendations() != null ? enhancedResult.getRecommendations() : new ArrayList<>();
				lastExperienceLevel = enhancedResult.getExperienceLevel() != null ? enhancedResult.getExperienceLevel() : "Unknown";
				lastExperienceGap = enhancedResult.getExperienceMatch();
				lastConfidenceScore = enhancedResult.getConfidenceScore();
				lastDetailedAnalysis = enhancedResult.getDetailedAnalysis() != null ? enhancedResult.getDetailedAnalysis() : new ArrayList<>();
				lastSkillFrequency = enhancedResult.getSkillFrequency() != null ? enhancedResult.getSkillFrequency() : new HashMap<>();
				
				System.out.println("Enhanced NLP Analysis completed successfully:");
				System.out.println("- Overall Score: " + (lastMatchPercent * 100) + "%");
				System.out.println("- Confidence Score: " + (lastConfidenceScore * 100) + "%");
				System.out.println("- Matched Skills: " + lastMatchedSkills.size());
				System.out.println("- Missing Skills: " + lastNotMatchedSkills.size());
				System.out.println("- Recommendations: " + lastRecommendations.size());
				System.out.println("- Experience Gap: " + lastExperienceGap + " years");
				System.out.println("- Skills Detected: " + lastSkillFrequency.size());
			} else {
				System.err.println("Enhanced NLP analysis returned null result");
				setDefaultResults();
			}
			
			// Store job details
			lastMatchedJobTitle = job.getTitle();
			lastMatchedJobCompany = job.getCompany();
			lastMatchedJobLocation = job.getLocation();
			lastMatchedJobSalary = String.format("Salary: $%.0f - $%.0f", job.getSalaryMin(), job.getSalaryMax());
			
		} catch (Exception ex) {
			System.err.println("Error in CV analysis: " + ex.getMessage());
			ex.printStackTrace();
			setDefaultResults();
		}
	}

	private void setDefaultResults() {
		lastMatchResult = null;
		lastEnhancedMatchResult = null;
		lastMatchPercent = 0.15; // Set to 15% instead of 0%
		lastMatchedSkills = Arrays.asList("Unable to detect skills");
		lastNotMatchedSkills = Arrays.asList("Please ensure CV contains clear skill descriptions");
		lastRecommendations = Arrays.asList(
			"Make sure your CV is readable and contains clear skill descriptions",
			"Include specific technologies and programming languages",
			"Add years of experience for each skill"
		);
		lastExperienceLevel = "Unable to determine";
		lastExperienceGap = 0;
		lastConfidenceScore = 0.0;
		lastDetailedAnalysis = Arrays.asList("Analysis failed - please check CV format and content");
		lastSkillFrequency = new HashMap<>();
	}

	public void populateMatchResults() {
		try {
			// Populate job details
			if (matchJobTitleLabel != null) matchJobTitleLabel.setText(lastMatchedJobTitle != null ? lastMatchedJobTitle : "No Job Selected");
			if (matchJobCompanyLabel != null) matchJobCompanyLabel.setText(lastMatchedJobCompany != null ? lastMatchedJobCompany : "No Company");
			if (matchJobLocationLabel != null) matchJobLocationLabel.setText(lastMatchedJobLocation != null ? lastMatchedJobLocation : "No Location");
			if (matchJobSalaryLabel != null) matchJobSalaryLabel.setText(lastMatchedJobSalary != null ? lastMatchedJobSalary : "Salary: N/A");
			
			// Populate match result with enhanced thresholds and confidence
			if (matchResultLabel != null) {
				String resultText;
				String confidenceText = String.format(" (Confidence: %.0f%%)", lastConfidenceScore * 100);
				
				if (lastMatchPercent >= 0.80) {
					resultText = "Excellent Match! 🎉" + confidenceText;
				} else if (lastMatchPercent >= 0.70) {
					resultText = "Strong Match ✓" + confidenceText;
				} else if (lastMatchPercent >= 0.60) {
					resultText = "Good Match ✓" + confidenceText;
				} else if (lastMatchPercent >= 0.40) {
					resultText = "Fair Match" + confidenceText;
				} else if (lastMatchPercent >= 0.20) {
					resultText = "Needs Improvement" + confidenceText;
				} else {
					resultText = "Poor Match - Consider Skills Development" + confidenceText;
				}
				
				matchResultLabel.setText(resultText);
				
				// Color coding based on confidence
				if (lastConfidenceScore >= 0.8) {
					matchResultLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
				} else if (lastConfidenceScore >= 0.6) {
					matchResultLabel.setStyle("-fx-text-fill: #F57C00; -fx-font-weight: bold;");
				} else {
					matchResultLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
				}
			}
			
			// Set progress and percentage
			if (matchProgressIndicator != null) {
				double progress = Math.max(lastMatchPercent, 0.05); // Minimum 5% for visibility
				matchProgressIndicator.setProgress(progress);
			}
			if (matchPercentageLabel != null) {
				matchPercentageLabel.setText(String.format("%.0f%%", lastMatchPercent * 100));
			}
			
			// Populate skills with better handling
			if (matchedSkillsBox != null) {
				matchedSkillsBox.getChildren().clear();
				if (lastMatchedSkills == null || lastMatchedSkills.isEmpty()) {
					Label noSkillsLabel = new Label("No clearly matched skills detected");
					noSkillsLabel.setStyle("-fx-text-fill: #757575;");
					matchedSkillsBox.getChildren().add(noSkillsLabel);
				} else {
					for (String skill : lastMatchedSkills) {
						Label skillLabel = new Label("✓ " + skill);
						skillLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
						matchedSkillsBox.getChildren().add(skillLabel);
					}
				}
			}
			
			if (notMatchedSkillsBox != null) {
				notMatchedSkillsBox.getChildren().clear();
				if (lastNotMatchedSkills == null || lastNotMatchedSkills.isEmpty()) {
					Label allMatchedLabel = new Label("All required skills appear to be covered!");
					allMatchedLabel.setStyle("-fx-text-fill: #2E7D32;");
					notMatchedSkillsBox.getChildren().add(allMatchedLabel);
				} else {
					for (String skill : lastNotMatchedSkills) {
						Label skillLabel = new Label("○ " + skill);
						skillLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");
						notMatchedSkillsBox.getChildren().add(skillLabel);
					}
				}
			}
			
			// Populate experience analysis
			if (experienceLevelLabel != null) {
				experienceLevelLabel.setText(lastExperienceLevel != null && !lastExperienceLevel.isEmpty() ? lastExperienceLevel : "Not specified");
			}
			if (experienceGapLabel != null) {
				String gapText;
				if (lastExperienceGap > 0) {
					gapText = "Need " + lastExperienceGap + " more years";
					experienceGapLabel.setStyle("-fx-text-fill: #D32F2F;");
				} else if (lastExperienceGap < 0) {
					gapText = "+" + Math.abs(lastExperienceGap) + " years above requirement";
					experienceGapLabel.setStyle("-fx-text-fill: #2E7D32;");
				} else {
					gapText = "Meets requirement";
					experienceGapLabel.setStyle("-fx-text-fill: #2E7D32;");
				}
				experienceGapLabel.setText(gapText);
			}
			
			// Populate recommendations with enhanced analysis
			if (recommendationsBox != null) {
				recommendationsBox.getChildren().clear();
				
				// Add confidence indicator
				if (lastConfidenceScore > 0) {
					Label confidenceLabel = new Label("📊 Analysis Confidence: " + String.format("%.0f%%", lastConfidenceScore * 100));
					confidenceLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5px 0px;");
					if (lastConfidenceScore >= 0.8) {
						confidenceLabel.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-padding: 5px 0px;");
					} else if (lastConfidenceScore >= 0.6) {
						confidenceLabel.setStyle("-fx-text-fill: #F57C00; -fx-font-weight: bold; -fx-padding: 5px 0px;");
					} else {
						confidenceLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold; -fx-padding: 5px 0px;");
					}
					recommendationsBox.getChildren().add(confidenceLabel);
				}
				
				// Add detailed analysis if available
				if (lastDetailedAnalysis != null && !lastDetailedAnalysis.isEmpty()) {
					Label analysisHeader = new Label("📋 Detailed Analysis:");
					analysisHeader.setStyle("-fx-font-weight: bold; -fx-padding: 10px 0px 5px 0px;");
					recommendationsBox.getChildren().add(analysisHeader);
					
					for (String analysis : lastDetailedAnalysis) {
						Label analysisLabel = new Label(analysis);
						analysisLabel.setWrapText(true);
						analysisLabel.setPrefWidth(900);
						analysisLabel.setStyle("-fx-padding: 2px 0px; -fx-font-size: 12px; -fx-text-fill: #666;");
						recommendationsBox.getChildren().add(analysisLabel);
					}
					
					// Add separator
					Label separator = new Label("────────────────────────────────────────");
					separator.setStyle("-fx-text-fill: #ccc; -fx-padding: 10px 0px;");
					recommendationsBox.getChildren().add(separator);
				}
				
				// Add recommendations
				Label recHeader = new Label("💡 Recommendations:");
				recHeader.setStyle("-fx-font-weight: bold; -fx-padding: 10px 0px 5px 0px;");
				recommendationsBox.getChildren().add(recHeader);
				
				if (lastRecommendations == null || lastRecommendations.isEmpty()) {
					Label noRecLabel = new Label("No specific recommendations available.");
					noRecLabel.setWrapText(true);
					noRecLabel.setStyle("-fx-padding: 5px 0px; -fx-font-size: 14px;");
					recommendationsBox.getChildren().add(noRecLabel);
				} else {
					for (String recommendation : lastRecommendations) {
						Label recLabel = new Label("• " + recommendation);
						recLabel.setWrapText(true);
						recLabel.setPrefWidth(900);
						recLabel.setStyle("-fx-padding: 5px 0px; -fx-font-size: 14px;");
						recommendationsBox.getChildren().add(recLabel);
					}
				}
			}
			
		} catch (Exception e) {
			System.err.println("Error populating match results: " + e.getMessage());
			e.printStackTrace();
		}
		
		// Control button visibility based on match results
		controlButtonVisibility();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (login_password != null && login_showpassword != null && login_selectshowPassword != null) {
			login_showpassword.managedProperty().bind(login_selectshowPassword.selectedProperty());
			login_showpassword.visibleProperty().bind(login_selectshowPassword.selectedProperty());
			login_password.managedProperty().bind(login_selectshowPassword.selectedProperty().not());
			login_password.visibleProperty().bind(login_selectshowPassword.selectedProperty().not());
			login_showpassword.textProperty().bindBidirectional(login_password.textProperty());
		}

		if (forgot_selectQuestion != null) {
			forgot_selectQuestion.getItems().addAll(
					"What is your pet's name?",
					"What is your mother's name?",
					"What was your first school?",
					"What is your favorite color?");
		}

		if (signup_selectQuestion != null) {
			signup_selectQuestion.getItems().addAll(
					"What is your pet's name?",
					"What is your mother's name?",
					"What was your first school?",
					"What is your favorite color?");
		}

		updateHomeUI();

		if (jobListContainer != null) {
			loadJobsFromAdzuna("");
		}

		if (jobSearchBtn != null && jobSearchField != null) {
			jobSearchBtn.setOnAction(e -> {
				String searchTerm = jobSearchField.getText();
				loadJobsFromAdzuna(searchTerm);
			});
		}

		// Populate JobDescriptionAdzuna.fxml if selectedAdzunaJob is set
		if (selectedAdzunaJob != null) {
			if (adzunaJobTitleLabel != null) adzunaJobTitleLabel.setText(selectedAdzunaJob.getTitle());
			if (adzunaJobTitleLabel2 != null) adzunaJobTitleLabel2.setText(selectedAdzunaJob.getTitle());
			if (adzunaJobCompanyLabel != null) adzunaJobCompanyLabel.setText("Company: " + selectedAdzunaJob.getCompany());
			if (adzunaJobCompanyLabel2 != null) adzunaJobCompanyLabel2.setText(selectedAdzunaJob.getCompany());
			if (adzunaJobLocationLabel != null) adzunaJobLocationLabel.setText("Location: " + selectedAdzunaJob.getLocation());
			if (adzunaJobLocationLabel2 != null) adzunaJobLocationLabel2.setText(selectedAdzunaJob.getLocation());
			if (adzunaJobSalaryLabel != null) adzunaJobSalaryLabel.setText("Salary: $" + selectedAdzunaJob.getSalaryMin() + " - $" + selectedAdzunaJob.getSalaryMax());
			if (adzunaJobSalaryLabel2 != null) adzunaJobSalaryLabel2.setText("$" + selectedAdzunaJob.getSalaryMin() + " - $" + selectedAdzunaJob.getSalaryMax());
			if (adzunaJobDescriptionContentLabel != null) adzunaJobDescriptionContentLabel.setText(selectedAdzunaJob.getDescription());
			if (adzunaSeeMoreBtn != null) {
				adzunaSeeMoreBtn.setOnAction(e -> {
					try {
						java.awt.Desktop.getDesktop().browse(new java.net.URI(selectedAdzunaJob.getUrl()));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
			}
			// Populate responsibilities and qualifications (simple split or placeholder logic)
			if (adzunaJobResponsibilitiesContentLabel != null) {
				String desc = selectedAdzunaJob.getDescription();
				String[] parts = desc.split("(?i)qualifications|requirements|skills|responsibilities");
				if (parts.length > 1) {
					adzunaJobResponsibilitiesContentLabel.setText(parts[0].trim());
					adzunaJobQualificationsContentLabel.setText(parts[1].trim());
				} else {
					adzunaJobResponsibilitiesContentLabel.setText(desc);
					adzunaJobQualificationsContentLabel.setText("See job description above.");
				}
			}
		}

		// Only populate match results if we're on the match results page AND have valid data
		if (matchJobTitleLabel != null && (lastEnhancedMatchResult != null || lastMatchResult != null)) {
			System.out.println("✅ Found valid results - populating UI");
			System.out.println("- Enhanced Result: " + (lastEnhancedMatchResult != null ? "Available" : "None"));
			System.out.println("- Basic Result: " + (lastMatchResult != null ? "Available" : "None"));
			System.out.println("- Match Percent: " + (lastMatchPercent * 100) + "%");
			populateMatchResults();
		} else if (matchJobTitleLabel != null) {
			// Set default/loading state
			System.out.println("⚠️ No valid results found - showing loading state");
			System.out.println("- Enhanced Result: " + (lastEnhancedMatchResult != null ? "Available" : "None"));
			System.out.println("- Basic Result: " + (lastMatchResult != null ? "Available" : "None"));
			System.out.println("- Match Percent: " + (lastMatchPercent * 100) + "%");
			setLoadingState();
		}
		
		// Force refresh if we have results but UI elements are null (page not fully loaded)
		if (matchJobTitleLabel == null && (lastEnhancedMatchResult != null || lastMatchResult != null)) {
			System.out.println("🔄 Results available but UI not loaded - will populate when page loads");
		}

		// Set up click handlers for navigation labels
		setupNavigationLabels();
	}

	@FXML
	private void handleChooseFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select PDF CV");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
		File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		if (file != null) {
			selectedFile = file;
			selectedFileLabel.setText(file.getName());
			errorLabel.setText("");
		} else {
			selectedFileLabel.setText("No file selected.");
		}
	}

	@FXML
	private void handleUploadCV(ActionEvent event) {
		if (selectedFile == null) {
			errorLabel.setText("Please select a PDF file and a job.");
			return;
		}
		if (selectedAdzunaJob == null) {
			errorLabel.setText("Please select a job first.");
			return;
		}

		lastMatchResult = null;

		analyzeCVAndMatch(selectedFile, selectedAdzunaJob);
		try {
			matchCriteriaResultPage(event);
		} catch (IOException e) {
			e.printStackTrace();
			errorLabel.setText("Error loading results page.");
		}
	}

	private void setupNavigationLabels() {
		// Home label click handler
		if (homeLabel != null) {
			homeLabel.setOnMouseClicked(event -> {
				navigateToHome();
			});
			homeLabel.setOnMouseEntered(event -> homeLabel.setCursor(Cursor.HAND));
			homeLabel.setOnMouseExited(event -> homeLabel.setCursor(Cursor.DEFAULT));
		}

		// Job label click handler
		if (jobLabel != null) {
			jobLabel.setOnMouseClicked(event -> {
				navigateToJobs();
			});
			jobLabel.setOnMouseEntered(event -> jobLabel.setCursor(Cursor.HAND));
			jobLabel.setOnMouseExited(event -> jobLabel.setCursor(Cursor.DEFAULT));
		}

		// Course label click handler
		if (courseLabel != null) {
			courseLabel.setOnMouseClicked(event -> {
				navigateToCourses();
			});
			courseLabel.setOnMouseEntered(event -> courseLabel.setCursor(Cursor.HAND));
			courseLabel.setOnMouseExited(event -> courseLabel.setCursor(Cursor.DEFAULT));
		}
	}

	private void setLoadingState() {
		if (matchJobTitleLabel != null) matchJobTitleLabel.setText("No job selected");
		if (matchJobCompanyLabel != null) matchJobCompanyLabel.setText("Please select a job first");
		if (matchJobLocationLabel != null) matchJobLocationLabel.setText("Location: N/A");
		if (matchJobSalaryLabel != null) matchJobSalaryLabel.setText("Salary: N/A");
		if (matchResultLabel != null) matchResultLabel.setText("Please upload your CV and select a job");
		if (matchProgressIndicator != null) matchProgressIndicator.setProgress(0.0);
		if (matchPercentageLabel != null) matchPercentageLabel.setText("0%");
		
		// Clear skill boxes
		if (matchedSkillsBox != null) {
			matchedSkillsBox.getChildren().clear();
			matchedSkillsBox.getChildren().add(new Label("Upload CV to see matched skills"));
		}
		if (notMatchedSkillsBox != null) {
			notMatchedSkillsBox.getChildren().clear();
			notMatchedSkillsBox.getChildren().add(new Label("Upload CV to see missing skills"));
		}
		
		// Clear other fields
		if (experienceLevelLabel != null) experienceLevelLabel.setText("Unknown");
		if (experienceGapLabel != null) experienceGapLabel.setText("Unknown");
		if (recommendationsBox != null) {
			recommendationsBox.getChildren().clear();
			recommendationsBox.getChildren().add(new Label("Upload your CV to get personalized recommendations"));
		}
		
		// Hide action buttons when no data is available
		if (applyForJobBtn != null) {
			applyForJobBtn.setVisible(false);
			applyForJobBtn.setManaged(false);
		}
		if (enrollInCourseBtn != null) {
			enrollInCourseBtn.setVisible(false);
			enrollInCourseBtn.setManaged(false);
		}
		if (enrollmentWarningContainer != null) {
			enrollmentWarningContainer.setVisible(false);
			enrollmentWarningContainer.setManaged(false);
		}
	}

	private void controlButtonVisibility() {
		try {
			// Define thresholds for button visibility
			final double GOOD_MATCH_THRESHOLD = 0.60; // 60% match
			final double MIN_SKILLS_THRESHOLD = 0.50; // At least 50% of required skills
			
			// Check if we have valid match results
			if ((lastEnhancedMatchResult == null && lastMatchResult == null) || lastMatchPercent <= 0.0) {
				// No valid results - hide both buttons
				if (applyForJobBtn != null) applyForJobBtn.setVisible(false);
				if (enrollInCourseBtn != null) enrollInCourseBtn.setVisible(false);
				if (enrollmentWarningContainer != null) enrollmentWarningContainer.setVisible(false);
				return;
			}
			
			// Calculate skill coverage percentage
			double skillCoverage = 0.0;
			if (lastMatchedSkills != null && lastNotMatchedSkills != null) {
				int totalRequiredSkills = lastMatchedSkills.size() + lastNotMatchedSkills.size();
				if (totalRequiredSkills > 0) {
					skillCoverage = (double) lastMatchedSkills.size() / totalRequiredSkills;
				}
			}
			
			// Determine if user has good match and sufficient skills
			boolean hasGoodMatch = lastMatchPercent >= GOOD_MATCH_THRESHOLD;
			boolean hasSufficientSkills = skillCoverage >= MIN_SKILLS_THRESHOLD;
			boolean shouldShowApplyButton = hasGoodMatch && hasSufficientSkills;
			
			// Control button visibility
			if (applyForJobBtn != null) {
				applyForJobBtn.setVisible(shouldShowApplyButton);
				applyForJobBtn.setManaged(shouldShowApplyButton);
			}
			
			if (enrollInCourseBtn != null) {
				enrollInCourseBtn.setVisible(!shouldShowApplyButton);
				enrollInCourseBtn.setManaged(!shouldShowApplyButton);
			}
			
			// Control warning container visibility
			if (enrollmentWarningContainer != null) {
				enrollmentWarningContainer.setVisible(!shouldShowApplyButton);
				enrollmentWarningContainer.setManaged(!shouldShowApplyButton);
			}
			
			// Add click handlers for the buttons
			if (applyForJobBtn != null && shouldShowApplyButton) {
				applyForJobBtn.setOnAction(e -> handleApplyForJob());
			}
			
			if (enrollInCourseBtn != null && !shouldShowApplyButton) {
				enrollInCourseBtn.setOnAction(e -> handleEnrollInCourse());
			}
			
			System.out.println("Button Visibility Logic:");
			System.out.println("- Match Percentage: " + (lastMatchPercent * 100) + "%");
			System.out.println("- Skill Coverage: " + (skillCoverage * 100) + "%");
			System.out.println("- Show Apply Button: " + shouldShowApplyButton);
			System.out.println("- Show Enroll Button: " + !shouldShowApplyButton);
			System.out.println("- Show Warning Container: " + !shouldShowApplyButton);
			
		} catch (Exception e) {
			System.err.println("Error controlling button visibility: " + e.getMessage());
			e.printStackTrace();
			// Fallback: hide both buttons on error
			if (applyForJobBtn != null) applyForJobBtn.setVisible(false);
			if (enrollInCourseBtn != null) enrollInCourseBtn.setVisible(false);
			if (enrollmentWarningContainer != null) enrollmentWarningContainer.setVisible(false);
		}
	}
	
	private void handleApplyForJob() {
		try {
			if (selectedAdzunaJob != null && selectedAdzunaJob.getUrl() != null) {
				// Open the job application URL in default browser
				java.awt.Desktop.getDesktop().browse(new java.net.URI(selectedAdzunaJob.getUrl()));
				showAlert(Alert.AlertType.INFORMATION, "Job Application", 
					"Opening job application page in your browser. Good luck with your application!");
			} else {
				showAlert(Alert.AlertType.WARNING, "No Job URL", 
					"Job application URL is not available. Please try another job.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", 
				"Could not open job application page. Please try again.");
		}
	}
	
	private void handleEnrollInCourse() {
		try {
			addUserToCourseEligible();
			showAlert(Alert.AlertType.INFORMATION, "Course Enrollment", 
				"Navigating to course page. Find relevant courses to improve your skills!");

			// Navigate to the course page
			Parent root = FXMLLoader.load(getClass().getResource("/Course.fxml"));
			Stage stage = (Stage) enrollInCourseBtn.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", 
				"Could not navigate to course page. Please try again.");
		}
	}

	private void navigateToHome() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
			Stage stage = (Stage) homeLabel.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to Home page.");
		}
	}

	private void navigateToJobs() {
		try {
			// Check if user is logged in
			if (!isLoggedIn) {
				// User is not logged in, redirect to login page
				showAlert(Alert.AlertType.INFORMATION, "Login Required", 
					"Please log in to access the job listings.");
				Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
				Stage stage = (Stage) jobLabel.getScene().getWindow();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
			}
			
			// User is logged in, proceed to jobs page
			Parent root = FXMLLoader.load(getClass().getResource("/JobDashBoardAdzuna.fxml"));
			Stage stage = (Stage) jobLabel.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to Jobs page.");
		}
	}

	private void navigateToCourses() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Course.fxml"));
			Stage stage = (Stage) courseLabel.getScene().getWindow();
			Stage currentStage = (Stage) courseLabel.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			currentStage.setScene(scene);
			currentStage.centerOnScreen();
			currentStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to Courses page.");
		}
	}

	@FXML
	private void handleWatchVideo(ActionEvent event) {
		try {
			showAlert(Alert.AlertType.INFORMATION, "Video", 
				"Opening Python tutorial video in your browser!");

			// Open Python tutorial video in default browser
			java.awt.Desktop.getDesktop().browse(new java.net.URI("https://youtu.be/kqtD5dpn9C8?si=55ACWBca6g4deqFE"));
		} catch (Exception e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", 
				"Could not open video. Please try again.");
		}
	}

	// Dynamic Profile Button Methods
	public void setUsername(String username) {
		if (username != null && !username.isEmpty()) {
			// Set the username as button text
			profileButton.setText(username);
			
			// Make button visible
			profileButton.setVisible(true);
			profileButton.setManaged(true);
			
			// Calculate and set dynamic width
			updateButtonWidth(username);
		}
	}

	// Method to calculate optimal button width
	private void updateButtonWidth(String username) {
		// Create a temporary Text node to measure text width
		javafx.scene.text.Text textNode = new javafx.scene.text.Text(username);
		textNode.setFont(profileButton.getFont());
		
		// Calculate text width and add padding
		double textWidth = textNode.getBoundsInLocal().getWidth();
		double padding = 30; // Adjust padding as needed (for arrow and margins)
		double minWidth = 80; // Minimum button width
		double maxWidth = 200; // Maximum button width to prevent extremely long buttons
		
		// Set the calculated width
		double buttonWidth = Math.max(minWidth, Math.min(maxWidth, textWidth + padding));
		profileButton.setPrefWidth(buttonWidth);
		profileButton.setMinWidth(buttonWidth);
	}

	// Alternative method using CSS styling for even more flexibility
	public void setUsernameWithCSS(String username) {
		if (username != null && !username.isEmpty()) {
			profileButton.setText(username);
			profileButton.setVisible(true);
			profileButton.setManaged(true);
			
			// Add CSS class for dynamic sizing
			profileButton.getStyleClass().add("dynamic-profile-button");
		}
	}

	// Method to handle long usernames with ellipsis
	public void setUsernameWithEllipsis(String username, int maxLength) {
		if (username != null && !username.isEmpty()) {
			String displayName = username;
			
			// Truncate if too long and add ellipsis
			if (username.length() > maxLength) {
				displayName = username.substring(0, maxLength - 3) + "...";
			}
			
			profileButton.setText(displayName);
			profileButton.setTooltip(new javafx.scene.control.Tooltip(username)); // Show full name on hover
			profileButton.setVisible(true);
			profileButton.setManaged(true);
			
			updateButtonWidth(displayName);
		}
	}
	
	// Chat functionality methods
	@FXML
	private void openChat(ActionEvent event) {
		if (!isLoggedIn || loggedInUser == null) {
			showAlert(Alert.AlertType.WARNING, "Login Required", "Please log in to access the chat.");
			return;
		}
		
		// Check if user is eligible for course chat
		if (!ChatServer.isUserEligibleForCourse(loggedInUser)) {
			showAlert(Alert.AlertType.WARNING, "Access Denied", 
				"You are not eligible for the course discussion group. Only users who were referred to courses can participate.");
			return;
		}
		
		// Open chat window
		ChatController.showChat(loggedInUser);
	}
	
	@FXML
	private void handleCoursesMenuItem(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/Course.fxml"));
			Stage stage = (Stage) profileButton.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not navigate to Courses page.");
		}
	}
	
	// Method to add user to course eligible when they are referred to courses
	private void addUserToCourseEligible() {
		if (loggedInUser != null && selectedAdzunaJob != null) {
			ChatServer.addUserToCourseEligible(
				loggedInUser, 
				selectedAdzunaJob.getTitle(), 
				selectedAdzunaJob.getCompany()
			);
		}
	}
}
