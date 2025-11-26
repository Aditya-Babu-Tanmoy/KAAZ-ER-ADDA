import java.awt.*; // For Layouts
import java.io.*; // For InputStream + BufferedReader
import java.net.HttpURLConnection; // URL connection[con]
import java.net.URI;
import java.net.URL; // For HTTP Connection
import java.util.ArrayList;
import javax.swing.*;
// Name is common but not workable
abstract class NameOnly {

    private String name; // Storing name

    public NameOnly(String name){
        this.name = name;
        }

    public String getName(){ // to get thename
        return name;
    }
    public abstract String getDetails();
}

class Freelancer extends NameOnly{

    private String skill;

    public Freelancer(String name, String skill){
        super(name);
        this.skill=skill; 
    }
    
    @Override 
    public String getDetails(){
        return "Freelancer: " +getName() + " with "+ skill + " skill";
    }
}

class Job extends NameOnly {
    private String req; // JOB's Requirement
    public Job(String title, String req){
        super(title); 
        this.req = req; 
    }

    @Override 
    public String getDetails(){ 
        return getName() + " needs " + req + " skill"; 
    }
}

public class Frame extends JFrame {
    
    ArrayList<NameOnly> database = new ArrayList<>(); // Use arraylist for dinamic size and remove easily
    JTextField tName = new JTextField(10); 
    JTextField tSkill = new JTextField(10);
    JTextArea tOutput = new JTextArea(); // Output Area(kicchu bola hoi nai karon output AI theke ashbe)

    public Frame() {
        setTitle("KAAZ-ER-ADDA");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());  // default layout

        JButton F = new JButton("Add Freelancer");
        JButton J = new JButton("Add Job");
        JButton S = new JButton("Assign Job");

        JPanel in = new JPanel();       // Crates a container for inputs as default lasyout
        in.add(new JLabel("Name/Title:"));
        in.add(tName);
        in.add(new JLabel("Skill/Requirement:"));
        in.add(tSkill);

        JPanel P = new JPanel();
        P.add(F);
        P.add(J);
        P.add(S);
        
        JPanel topContainer = new JPanel(new GridLayout(2, 3));
        topContainer.add(in);
        topContainer.add(P);

        tOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(tOutput);
        
        add(topContainer, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        F.addActionListener(e -> {
            database.add(new Freelancer(tName.getText(), tSkill.getText()));
            tOutput.append("Added Freelancer: " + tName.getText()+ " with " + tSkill.getText() + " skill\n");
            clearFields();
        });

        J.addActionListener(e -> {
            database.add(new Job(tName.getText(), tSkill.getText()));
            tOutput.append("Posted Job: "+ tName.getText()+"\n");
            clearFields();
        });

        S.addActionListener(e -> runGeminiAI());
    }

    void clearFields() {
        tName.setText(""); 
        tSkill.setText("");
    }

    void runGeminiAI() {
        if(database.isEmpty()) {
            tOutput.append("Sorry nothing to match\n"); 
            return; 
        }
        tOutput.append("\n--- PROCESSING... ---\n");
            try { // if API key does not work do not crash
                StringBuilder prompt = new StringBuilder("Match these freelancers to jobs based on skills. Return a clean list of who gets what job: If the skill doesnot match Simply print Doesnot match with any frelancer message. Do not use * this sign in output section.\n");
                for(NameOnly e : database)
                    prompt.append(e.getDetails()).append("\n");
                

                //URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=AIzaSyBMhCtpTqGlICzRSgedAv2YS8ut99mFmmE");
                URI uri = new URI("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=AIzaSyBMhCtpTqGlICzRSgedAv2YS8ut99mFmmE");
                URL url = uri.toURL();
                
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String cleanPrompt = prompt.toString().replace("\"", "'").replace("\n", "\\n");
                String json = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + cleanPrompt + "\" }] }] }";


                con.getOutputStream().write(json.getBytes("UTF-8"));
                int status = con.getResponseCode();
                InputStream stream = (status == 200) ? con.getInputStream() : con.getErrorStream();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while((line = br.readLine()) != null)
                    response.append(line);
                
                if (status != 200) {
                    tOutput.append("API ERROR (" + status + " " + response.toString() + "\n");
                    return;
                }

                String raw = response.toString();
                int startMarker = raw.indexOf("\"text\":");
                if (startMarker != -1) {
                    String value = raw.substring(startMarker + 7); // Skip past "text":
                    int startQuote = value.indexOf("\"");
                    int endQuote = value.lastIndexOf("\""); 
                    
                    if (startQuote != -1 && endQuote > startQuote) {
                        // Extract text between quotes and unescape characters
                        String result = value.substring(startQuote + 1, endQuote);
                        tOutput.append(result.replace("\\n", "\n").replace("\\\"", "\"") + "\n----------------\n");
                        return;
                    }
                }
                
                tOutput.append("Raw Response (Parser failed): " + raw + "\n");

            }
            catch (Exception ex){
                tOutput.append("Connection Error: " + ex.getMessage() + "\n");
            }
    }

    public static void main(String[] args) {
        
        new Frame().setVisible(true);
    }
}