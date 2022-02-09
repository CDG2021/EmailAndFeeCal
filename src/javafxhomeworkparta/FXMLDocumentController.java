package javafxhomeworkparta;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Carlos Garcia
 */
public class FXMLDocumentController implements Initializable {
    int mealValue=0, DormValue = 0, optionTotal = 0;
    double GrandTotal=0;
    String dormChoosen = "";
    String mealChoice = "";
    String laundryR = "";
    String ppArea = "";
    String buses = "";
    DateTimeFormatter ltf = DateTimeFormatter.ofPattern("hh:mm:ss");
    @FXML
    private Label emailSend;
    
    //setup for the combobox
    @FXML
    private ComboBox combo;

    @FXML
    void Select(ActionEvent event) {
        try{
            String s = combo.getSelectionModel().getSelectedItem().toString();
            
            if(s == "Allen Hall: $1,800")
        {
            dormChoosen = s + "\n";
            DormValue = 1800;
        }
        else if(s=="Pike Hall: $2,200")
        {
            dormChoosen = s + "\n";
            DormValue = 2200;
        }
        else if(s=="Farthing Hall: $2,800")
        {
            dormChoosen = s + "\n";
            DormValue = 2800;
        }
        else if(s=="University Suites: $3,000")
        {
            dormChoosen = s + "\n";
            DormValue = 3000;
        }
        }
        
        catch(Exception e){
        }
    }
    
    void getSelectedDorm(){
        try{
            String s = combo.getSelectionModel().getSelectedItem().toString();
            
            if(s == "Allen Hall: $1,800")
        {
            dormChoosen = s + "\n";
            DormValue = 1800;
        }
        else if(s=="Pike Hall: $2,200")
        {
            dormChoosen = s + "\n";
            DormValue = 2200;
        }
        else if(s=="Farthing Hall: $2,800")
        {
            dormChoosen = s + "\n";
            DormValue = 2800;
        }
        else if(s=="University Suites: $3,000")
        {
            dormChoosen = s + "\n";
            DormValue = 3000;
        }
        }
        
        catch(Exception e){
        }
    }
    //Sets up the radio button mealplan
    @FXML
    private RadioButton mealPlan1Radio;
    @FXML
    private RadioButton mealPlan2Radio;
    @FXML
    private RadioButton mealPlan3Radio;
    @FXML
    private ToggleGroup mealGroup;
    
      
    public void radioButtonValue(){
        if(mealPlan1Radio.isSelected())
        {
            mealChoice = "7 meals per week: $600" + "\n";
            mealValue= 600;
        }
        else if(mealPlan2Radio.isSelected()){
            mealChoice = "14 meals per week: $1,100" + "\n";
            mealValue = 1100;
        }
        else if(mealPlan3Radio.isSelected()){
            mealChoice = "Unlimited meals: $1,800" + "\n";
            mealValue = 1800;
        }
    }
    
    @FXML
    private Button placeOrder;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private CheckBox emailCheck;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> list = FXCollections.observableArrayList("Allen Hall: $1,800", "Pike Hall: $2,200", "Farthing Hall: $2,800", "University Suites: $3,000");
        combo.setItems(list);
        placeOrder.setOnAction(event ->
        {
            try {
                if( emailField.getText().isEmpty())
                {
                    System.out.println("Nothing in here");
                }
                else if(emailCheck.isSelected() == true){
                    if(paymentButtonValue().matches("Error occurred"))
                    {
                        paymentChoosen.setText("Please select a Payment Type!");
                    }
                    else{
                        radioButtonValue();
                        totalOptions();
                        getSelectedDorm();
                        sendMail(emailField.getText());
                    }
                }
                else{
                    System.out.println("Not checked");
                }
            } catch (MessagingException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }  
    public void sendMail(String recepient) throws MessagingException
    {
        System.out.println("Preparing to send");
        Properties properties = new Properties();
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        
        //Email goes in here
        String myAccountEmail = "";
        //Password goes in here
        String password = "";
        Session seasion = Session.getInstance(properties, new Authenticator()
        {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
        });
        
        Message message = prepareMessage(seasion, myAccountEmail, recepient);
        Transport.send(message);
        System.out.println("Message sent!");
    }
    
    private Message prepareMessage(Session s, String myEmail, String recepient) {
        try {
            Message message = new MimeMessage(s);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            LocalDateTime current = LocalDateTime.now();
            message.setSubject("Your Order places at " + current.getMonthValue()+"/" + current.getDayOfMonth()+"/" + current.getYear() + " " + current.format(ltf));
            GrandTotal = mealValue + DormValue + optionTotal;
            String output = NumberFormat.getInstance(Locale.US).format(GrandTotal);
            if(GrandTotal % 1 == 0)
            {
                output = "Total Cost: $" + output + ".00";
            }
            else
            {
                output = "Total Cost: $" + output;
            }
            message.setText("You have selected the following items or options:" + "\n" + dormChoosen + mealChoice + laundryR + ppArea + buses + "\n\n" + output+"\n" + paymentButtonValue() + "\n"+ "Thank you for your business!");
            totalCostLabel.setText(output);
            emailSend.setText("Email sent!");
            paymentChoosen.setText(paymentButtonValue());
            resetInitialValues();
            return message;
        } catch (Exception ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //sets up the radio button payment plan
    @FXML
    private RadioButton cashPayment;
    @FXML
    private RadioButton checkPayment;
    @FXML
    private RadioButton creditCardPayment;
    @FXML
    private ToggleGroup paymentGroup;
    
    public String paymentButtonValue(){
        if(cashPayment.isSelected())
            return "Pay by Cash";
        else if(checkPayment.isSelected())
            return "Pay by Check";
        else if(creditCardPayment.isSelected())
            return "Pay by Credit Card";
        else
            return "Error occurred";
    }
    //sets up the option checkbox
    @FXML
    private CheckBox option1;
    @FXML
    private CheckBox option2;
    @FXML
    private CheckBox option3;
    
    public void totalOptions(){
        if(option1.isSelected())
        {
            laundryR = "Use laundry room: $80" + "\n";
            optionTotal = optionTotal+80;
        }
        if(option2.isSelected())
        {
            ppArea = "Use paid-parking areas: $90" + "\n";
            optionTotal = optionTotal+90;
        }
        if(option3.isSelected()){
            buses = "Use SE school buses: $100";
            optionTotal = optionTotal+100;
        }
    }
    //Sets up cleaing all the information on the screen
    
    @FXML
    void buttonPressed(ActionEvent event) {
        resetInitialValues();
        paymentGroup.selectToggle(null);
        mealGroup.selectToggle(null);
        combo.getSelectionModel().clearSelection();
        emailCheck.setSelected(false);
        emailField.setText("");
        DormValue = 0;
        paymentChoosen.setText(null);
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);
        totalCostLabel.setText("Total Cost: ");
        emailSend.setText("");
    }
    
    //compute total cost
    @FXML
    private Label paymentChoosen;
    
    @FXML
    private Label totalCostLabel;
     
    @FXML
    void computeTotal(ActionEvent event) {
        radioButtonValue();
        totalOptions();
        emailSend.setText("");
        GrandTotal = mealValue + DormValue + optionTotal;
        System.out.println(paymentButtonValue());
        if(paymentButtonValue() == "Error occurred")
        {
            paymentChoosen.setText("Please select a Payment Type!");
            resetInitialValues();
        }
        else if(GrandTotal == 0)
        {
            totalCostLabel.setText("Total Cost: ");
            paymentChoosen.setText("");
            resetInitialValues();
        }
        else {
            String output = NumberFormat.getInstance(Locale.US).format(GrandTotal);
            if(GrandTotal % 1 == 0)
            {
                output = "Total Cost: $" + output + ".00";
            }
            else
            {
                output = "Total Cost: $" + output;
            }
            totalCostLabel.setText(output);
            paymentChoosen.setText(paymentButtonValue());
            resetInitialValues();
        }
    }
    public void resetInitialValues(){
        GrandTotal = 0;
        mealValue = 0;
        optionTotal = 0;
        dormChoosen ="";
        mealChoice="";
        laundryR="";
        ppArea="";
        buses="";
    }

    
}

