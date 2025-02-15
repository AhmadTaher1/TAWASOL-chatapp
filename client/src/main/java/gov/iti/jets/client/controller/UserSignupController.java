package gov.iti.jets.client.controller;

import gov.iti.jets.client.ClientMain;
import gov.iti.jets.client.model.ClientImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import shared.dto.User;
import shared.interfaces.AdminInt;
import shared.interfaces.UserInt;
import shared.utils.Hashing_UtilityClass;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class UserSignupController {

    private UserInt userInt;
    private AdminInt adminInt;
    ClientImpl c;
    private User currentUser = null;

    public void setUserInt(UserInt userInt) {
        this.userInt = userInt;
    }

    public void setAdminInt(AdminInt adminInt) {
        this.adminInt = adminInt;
    }

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField email;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private ComboBox<String> gender;

    @FXML
    private ComboBox<String> country;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Button button;


    private ObservableList<String> countries = FXCollections.observableArrayList(
            "Afghanistan",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Australia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cabo Verde",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Central African Republic",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo (Congo-Brazzaville)",
            "Costa Rica",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czechia (Czech Republic)",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor (Timor-Leste)",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Eswatini (fmr. Swaziland)",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Italy",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea, North",
            "Korea, South",
            "Kosovo",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Morocco",
            "Mozambique",
            "Myanmar (formerly Burma)",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "North Macedonia",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine State",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Togo",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States of America",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Zambia",
            "Zimbabwe"
    );

    @FXML
    public void initialize() {
        c = ClientImpl.getInstance();
        c.setUserSignupController(this);

        gender.getItems().addAll("Male", "Female");

        country.setItems(countries);

        // Make the country ComboBox searchable
        FilteredList<String> filteredCountries = new FilteredList<>(countries, p -> true);
        country.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {

            // If the change is due to selecting an item, skip updating the filter
            if (country.getSelectionModel().getSelectedItem() != null &&
                    country.getSelectionModel().getSelectedItem().equals(newValue)) {
                return;
            }


            filteredCountries.setPredicate(country -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return country.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // Bind the filtered list to the ComboBox
        country.setItems(filteredCountries);
    }

    @FXML
    private void handleSignup() {

        try {
            if (adminInt.getServerStatus() == true) {
                // Validate that all fields are filled
                boolean isValid = validateFields();

                if (!isValid) {
                    showErrorAlert("Empty Fields", "All fields are required. Please fill out all fields.");
                    return;
                }

                // Validate phone number (must contain only numbers)
                if (!isValidPhoneNumber(phoneNumber.getText())) {
                    showErrorAlert("Invalid Phone Number", "Please enter a valid 11-digit phone number");
                    phoneNumber.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
                    return;
                } else {
                    phoneNumber.setStyle("");
                }

                try {
                    if (isUsedPhoneNumber(phoneNumber.getText())) {
                        showErrorAlert("Phone Number", "The phone number already exists.");
                        phoneNumber.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
                        return;
                    } else {
                        phoneNumber.setStyle("");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }


                // Validate email (must be in a valid format)
                if (!isValidEmail(email.getText())) {
                    showErrorAlert("Invalid Email", "Please enter a valid email address.");
                    email.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
                    return;
                } else {
                    email.setStyle("");
                }

                try {
                    if (isUsedEmail(email.getText())) {
                        showErrorAlert("Email", "Email is already exists.");
                        email.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
                        return;
                    } else {
                        phoneNumber.setStyle("");
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }


                // Validate password and confirm password
                if (!password.getText().equals(confirmPassword.getText())) {
                    showErrorAlert("Password Mismatch", "The password and confirm password fields do not match.");
                    return;
                }

                // If all fields are filled and passwords match, proceed with signup logic
                String selectedGender = gender.getValue();
                String selectedCountry = country.getValue();


                User newUser = new User();
                newUser.setLastSeen(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)));
                newUser.setEmail(email.getText());
                newUser.setBio("I am a new user");
                newUser.setCountry(selectedCountry);
                newUser.setDisplayName(firstName.getText() + " " + lastName.getText());
                newUser.setGender(User.Gender.valueOf(selectedGender.toLowerCase()));
                newUser.setPasswordHash(Hashing_UtilityClass.hashString(password.getText()));
                newUser.setPhoneNumber(phoneNumber.getText());
                newUser.setDateOfBirth(Date.valueOf(dateOfBirth.getValue()));
                newUser.setStatus(User.Status.AVAILABLE);
                newUser.setProfilePicturePath("/img/man.png");

                try {
                    userInt.addUsertoDB(newUser);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("new User is created successfully!!!");
                try {
                    currentUser = userInt.isValidUser(phoneNumber.getText(), password.getText());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/homeScreen.fxml"));

                    Parent signupRoot = loader.load();
                    HomeScreenController homeScreenController = loader.getController();
                    homeScreenController.setUserInt(ClientMain.userInt);
                    homeScreenController.setAdminInt(ClientMain.adminInt);
                    homeScreenController.setCurrentUser(currentUser);


                    Stage stage = (Stage) button.getScene().getWindow();
                    double width = stage.getWidth();
                    double height = stage.getHeight();

                    Scene scene = new Scene(signupRoot);
                    stage.setScene(scene);
                    stage.setWidth(width);
                    stage.setHeight(height);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("server is off");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ServerUnavailable.fxml"));

                Parent root = loader.load();
                ServerUnavailableController serverUnavailableController = loader.getController();
                serverUnavailableController.setAdminInt(ClientMain.adminInt);
                serverUnavailableController.setUserInt(ClientMain.userInt);
                serverUnavailableController.setCurrentUser(currentUser);
                serverUnavailableController.setNavigatedWindow(gov.iti.jets.client.controller.ServerUnavailableController.Window.SIGNUP_PAGE);

                Stage stage = this.getStage();

                // Set the scene with the admin login page
                Scene scene = new Scene(root);
                stage.setScene(scene);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to validate all fields
    private boolean validateFields() {
        boolean isValid = true;


        if (firstName.getText().isEmpty()) {
            firstName.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            firstName.setStyle("");
        }


        if (lastName.getText().isEmpty()) {
            lastName.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            lastName.setStyle("");
        }


        if (phoneNumber.getText().isEmpty()) {
            phoneNumber.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            phoneNumber.setStyle("");
        }


        if (email.getText().isEmpty()) {
            email.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            email.setStyle("");
        }


        if (dateOfBirth.getValue() == null) {
            dateOfBirth.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            dateOfBirth.setStyle("");
        }


        if (gender.getValue() == null) {
            gender.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            gender.setStyle("");
        }


        if (country.getValue() == null) {
            country.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            country.setStyle("");
        }


        if (password.getText().isEmpty()) {
            password.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            password.setStyle("");
        }


        if (confirmPassword.getText().isEmpty()) {
            confirmPassword.setStyle("-fx-border-color: #dc3545; -fx-border-width: 2px;");
            isValid = false;
        } else {
            confirmPassword.setStyle("");
        }

        return isValid;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regex to match exactly 11 digits
        String regex = "^(010|011|012|015)[0-9]{8}$";
        return phoneNumber.matches(regex);
    }

    private boolean isValidEmail(String email) {
        // Regex for a basic email validation
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isUsedPhoneNumber(String phoneNumber) throws RemoteException {
        return userInt.isUserFoundByPhoneNumber(phoneNumber);
    }

    private boolean isUsedEmail(String email) throws RemoteException {
        return userInt.isUserFoundByEmail(email);
    }

    public Stage getStage() {
        return (Stage) button.getScene().getWindow();
    }

}