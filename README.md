# 🧑‍💻 KAAZ-ER-ADDA: Freelancer & Job Matching System

KAAZ-ER-ADDA (which translates roughly to "Workspace Hangout" or "Work Spot") is a simple Java Swing system designed to match **Freelancers** with available **Jobs** based on their respective skills and requirements.

The core matching intelligence is powered by the **Google Gemini API**, which processes the current list of freelancers and jobs to find the optimal assignments.

## ✨ Key Features

* **GUI Interface:** A user-friendly Java Swing-based graphical interface.
* **Object-Oriented Design:** Uses inheritance (`NameOnly` abstract class) to manage both `Freelancer` and `Job` entities uniformly.
* **Gemini API Integration:** Sends collected data to the `gemini-2.5-flash` model to perform skill-based matching and assignment.

## 🚀 Getting Started

### Prerequisites

1.  **Java Development Kit (JDK):** Ensure you have a recent version of Java installed (JDK 8 or higher).
2.  **Gemini API Key:** You must have a Gemini API Key.

## 📐 Code Structure

| Class | Type | Purpose |
| :--- | :--- | :--- |
| `NameOnly` | Abstract Class | Base class for all entities, storing the `name`/`title` and defining the abstract `getDetails()` method. |
| `Freelancer` | Concrete Class | Represents a freelancer with a `name` and their specific `skill`. |
| `Job` | Concrete Class | Represents a job with a `title` and its required `req` (skill requirement). |
| `Frame` | JFrame/Main App | Manages the GUI, holds the `database` (`ArrayList<NameOnly>`), and handles the API communication logic in `runGeminiAI()`. |

### The Matching Process (`runGeminiAI` method)

1.  **Data Collection:** Iterates through the `database` (which holds both Freelancers and Jobs) and calls `getDetails()` on each object to create a comprehensive text prompt.
2.  **API Call:** Sends the prompt to the Gemini API endpoint.
3.  **Response Handling:** Reads the API response, parses the JSON to extract the clean text, and displays the final assignment list in the output area.

### Highlight
It is the combination of Object orited programming and my Language Model(LM) trainning knowledge.
