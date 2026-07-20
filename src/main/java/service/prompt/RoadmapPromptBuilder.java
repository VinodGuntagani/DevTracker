package service.prompt;

public class RoadmapPromptBuilder {

    public String build(String userPrompt, int days, int dailyMinutes) {

        String prompt = """
                You are an expert career mentor and study planner.

                Create a complete realistic learning roadmap.

                Rules:

                1. Create main subjects.
                2. Create topics inside subjects.
                3. Create subtopics inside topics.
                4. Estimate difficulty.
                5. Estimate learning minutes for every subtopic.
                6. Use the user's daily available study time.
                7. Total study minutes per day must not exceed user's daily limit.
                8. Allocate every subtopic to a day number.
                9. Finish within given days.
                10. Order by prerequisites.
                11. Return ONLY JSON.
                12. Minutes must be integer values.
                13. Use multiples of 5 minutes.
        		14.if you making part of something give some details in the bracket so we know what part is cover and what should be covered in that part 
                JSON format:

                {
                  "title":"",
                  "description":"",
                  "subjects":[
                    {
                      "name":"",
                      "topics":[
                        {
                          "name":"",
                          "subtopics":[
                            {
                              "name":"",
                              "difficulty":"Easy",
                              "minutes":60,
                              "day":1
                            }
                          ]
                        }
                      ]
                    }
                  ]
                }

                User goal:
                """;

        return prompt
                + userPrompt
                + """

                Available days:
                """
                + days
                + """

                Available study minutes per day:
                """
                + dailyMinutes;
    }

}