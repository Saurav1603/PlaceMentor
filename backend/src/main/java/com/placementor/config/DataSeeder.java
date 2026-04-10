package com.placementor.config;

import com.placementor.model.Question;
import com.placementor.model.User;
import com.placementor.repository.QuestionRepository;
import com.placementor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Seed admin user if not exists
        if (!userRepository.existsByEmail("admin@placementor.com")) {
            userRepository.save(User.builder()
                    .name("Admin")
                    .email("admin@placementor.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .goal(User.Goal.PLACEMENT)
                    .dailyStudyHours(8)
                    .build());
            System.out.println("✓ Admin user created: admin@placementor.com / admin123");
        }

        // Seed questions if DB is empty
        if (questionRepository.count() == 0) {
            seedQuestions();
            System.out.println("✓ Seeded " + questionRepository.count() + " questions");
        }
    }

    private void seedQuestions() {
        List<Question> questions = List.of(
            // Arrays - Easy
            q("Arrays", "EASY", "What is the time complexity of accessing an element in an array by index?", "O(n)", "O(1)", "O(log n)", "O(n²)", "B"),
            q("Arrays", "EASY", "Which of the following is used to find the maximum element in an array?", "Binary Search", "Linear Search", "Hashing", "Sorting", "B"),
            q("Arrays", "EASY", "What is the default value of an int array in Java?", "null", "0", "-1", "undefined", "B"),
            // Arrays - Medium
            q("Arrays", "MEDIUM", "What is the best approach for finding a pair with a given sum in a sorted array?", "Brute Force", "Two Pointers", "Recursion", "DFS", "B"),
            q("Arrays", "MEDIUM", "Kadane's algorithm is used to solve which problem?", "Sorting", "Maximum Subarray Sum", "Searching", "Graph Traversal", "B"),
            // Arrays - Hard
            q("Arrays", "HARD", "What is the time complexity of the best algorithm for finding the median of two sorted arrays?", "O(n)", "O(n log n)", "O(log(min(m,n)))", "O(m+n)", "C"),

            // Graphs - Easy
            q("Graphs", "EASY", "Which data structure is used for BFS?", "Stack", "Queue", "Array", "Tree", "B"),
            q("Graphs", "EASY", "What is the space complexity of an adjacency matrix?", "O(V)", "O(E)", "O(V²)", "O(V+E)", "C"),
            q("Graphs", "EASY", "A graph with no cycles is called?", "Complete Graph", "Bipartite Graph", "Acyclic Graph", "Eulerian Graph", "C"),
            // Graphs - Medium
            q("Graphs", "MEDIUM", "Which algorithm finds shortest path in a weighted graph with no negative edges?", "BFS", "DFS", "Dijkstra", "Bellman-Ford", "C"),
            q("Graphs", "MEDIUM", "Topological sorting is applicable to which type of graph?", "Undirected", "Cyclic", "DAG", "Complete", "C"),
            // Graphs - Hard
            q("Graphs", "HARD", "What is the time complexity of Kruskal's algorithm?", "O(V²)", "O(E log V)", "O(E log E)", "O(V+E)", "C"),

            // DP - Easy
            q("DP", "EASY", "What is the base case for Fibonacci using DP?", "F(0)=0, F(1)=1", "F(0)=1, F(1)=1", "F(1)=0, F(2)=1", "F(0)=1, F(1)=0", "A"),
            q("DP", "EASY", "DP is mainly an optimization over which technique?", "Greedy", "Divide and Conquer", "Recursion", "Backtracking", "C"),
            q("DP", "EASY", "Which approach builds solution from smaller subproblems?", "Top-down", "Bottom-up", "Both A and B", "Neither", "C"),
            // DP - Medium
            q("DP", "MEDIUM", "What is the time complexity of the 0/1 Knapsack problem?", "O(n)", "O(nW)", "O(2^n)", "O(n²)", "B"),
            q("DP", "MEDIUM", "LCS stands for?", "Longest Common Subarray", "Longest Common Subsequence", "Least Common Subsequence", "Longest Contiguous Sequence", "B"),
            // DP - Hard
            q("DP", "HARD", "Matrix Chain Multiplication has a time complexity of?", "O(n²)", "O(n³)", "O(2^n)", "O(n log n)", "B"),

            // Trees - Easy
            q("Trees", "EASY", "A binary tree with n nodes has how many edges?", "n", "n-1", "n+1", "2n", "B"),
            q("Trees", "EASY", "Which traversal visits root first?", "Inorder", "Postorder", "Preorder", "Level-order", "C"),
            q("Trees", "EASY", "What is the maximum number of nodes at level L of a binary tree?", "L", "2L", "2^L", "L²", "C"),
            // Trees - Medium
            q("Trees", "MEDIUM", "What is the height of a balanced BST with n nodes?", "O(n)", "O(log n)", "O(n²)", "O(1)", "B"),
            q("Trees", "MEDIUM", "Which technique is used for level-order traversal?", "Stack", "Queue", "Recursion", "Sorting", "B"),
            // Trees - Hard
            q("Trees", "HARD", "What is the worst-case time complexity for search in an AVL tree?", "O(n)", "O(log n)", "O(n log n)", "O(1)", "B"),

            // Sorting - Easy
            q("Sorting", "EASY", "What is the time complexity of Bubble Sort?", "O(n)", "O(n log n)", "O(n²)", "O(log n)", "C"),
            q("Sorting", "EASY", "Which sorting algorithm is stable?", "Quick Sort", "Heap Sort", "Merge Sort", "Selection Sort", "C"),
            q("Sorting", "EASY", "What is the best-case time complexity of Insertion Sort?", "O(n²)", "O(n)", "O(n log n)", "O(1)", "B"),
            // Sorting - Medium
            q("Sorting", "MEDIUM", "What is the average time complexity of Quick Sort?", "O(n)", "O(n²)", "O(n log n)", "O(log n)", "C"),
            q("Sorting", "MEDIUM", "Which sorting algorithm uses a divide and conquer approach?", "Bubble Sort", "Insertion Sort", "Merge Sort", "Selection Sort", "C"),
            // Sorting - Hard
            q("Sorting", "HARD", "What is the time complexity of Radix Sort?", "O(n²)", "O(n log n)", "O(nk)", "O(n)", "C")
        );

        questionRepository.saveAll(questions);
    }

    private Question q(String topic, String difficulty, String text, String a, String b, String c, String d, String correct) {
        return Question.builder()
                .topic(topic)
                .difficulty(Question.Difficulty.valueOf(difficulty))
                .questionText(text)
                .optionA(a)
                .optionB(b)
                .optionC(c)
                .optionD(d)
                .correctAnswer(correct)
                .build();
    }
}
