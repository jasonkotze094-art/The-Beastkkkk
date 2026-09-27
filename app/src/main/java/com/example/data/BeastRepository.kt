package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object BeastRepository {

    // Subjects Overview
    val subjectOverviews = listOf(
        SubjectOverview(
            name = "Mathematics",
            questionCount = 18,
            conceptCount = 11,
            highPriority = listOf("Algebra", "Trigonometry"),
            reviewAreas = listOf("Quadratics", "Calculus Limits"),
            averageScore = 84
        ),
        SubjectOverview(
            name = "Physical Sciences",
            questionCount = 14,
            conceptCount = 9,
            highPriority = listOf("Chemical reactions", "Forces & motion"),
            reviewAreas = listOf("Newton's laws", "Organic chemistry"),
            averageScore = 76
        ),
        SubjectOverview(
            name = "English",
            questionCount = 10,
            conceptCount = 7,
            highPriority = listOf("Comprehension", "Critical Analysis"),
            reviewAreas = listOf("Direct/Indirect Speech", "Literary Devices"),
            averageScore = 91
        )
    )

    // Chat Sources from prompt
    val initialChatSources = listOf(
        ChatSource("c1", "Chat 1", 92, 6, "Past Exam Paper 2024 Analysis"),
        ChatSource("c2", "Chat 2", 78, 5, "Newtonian Mechanics & Kinetics"),
        ChatSource("c3", "Chat 3", 86, 7, "Organic Chemistry Nomenclature"),
        ChatSource("c4", "Chat 4", 64, 4, "Trigonometric Identities & Reduction"),
        ChatSource("c5", "Chat 5", 95, 5, "Calculus & Quadratic Equations")
    )

    // Initial Question Bank (42 questions structured across Math, Physics, English)
    private val rawQuestions: List<Question> = buildList {
        // Mathematics (18 questions)
        add(
            Question(
                id = "m1",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Quadratics",
                questionText = "Solve for x: 2x² - 5x - 3 = 0",
                options = listOf("x = 3 or x = -1/2", "x = -3 or x = 1/2", "x = 2 or x = -3", "x = 1/2 or x = -3"),
                correctAnswerIndex = 0,
                explanation = "(2x + 1)(x - 3) = 0 gives x = -1/2 or x = 3.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m2",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Quadratics",
                questionText = "Determine the nature of the roots for the equation 3x² + 4x + 5 = 0.",
                options = listOf("Real and equal", "Non-real (complex)", "Rational and unequal", "Irrational and unequal"),
                correctAnswerIndex = 1,
                explanation = "Discriminant Δ = b² - 4ac = 16 - 4(3)(5) = 16 - 60 = -44 < 0, hence roots are non-real.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m3",
                subject = "Mathematics",
                topic = "Trigonometry",
                concept = "Trigonometric Identities",
                questionText = "Simplify the expression: sin(180° - θ) · cos(90° - θ) + cos²(θ)",
                options = listOf("0", "1", "2sin²(θ)", "tan(θ)"),
                correctAnswerIndex = 1,
                explanation = "sin(180° - θ) = sin(θ), cos(90° - θ) = sin(θ). So sin²(θ) + cos²(θ) = 1.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m4",
                subject = "Mathematics",
                topic = "Trigonometry",
                concept = "Reduction Formulae",
                questionText = "What is the general solution for cos(2x) = 0.5 in the domain [0°, 360°]?",
                options = listOf("x = 30° + k·180° or x = 150° + k·180°", "x = 60° + k·360°", "x = 30° + k·360°", "x = 45° + k·180°"),
                correctAnswerIndex = 0,
                explanation = "2x = ±60° + k·360° => x = ±30° + k·180°.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m5",
                subject = "Mathematics",
                topic = "Calculus",
                concept = "Limits & Derivatives",
                questionText = "Evaluate lim(x→2) [ (x² - 4) / (x - 2) ]",
                options = listOf("0", "2", "4", "Undefined"),
                correctAnswerIndex = 2,
                explanation = "(x - 2)(x + 2) / (x - 2) = x + 2. When x -> 2, limit = 4.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "m6",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Exponents & Surds",
                questionText = "Simplify: (2^(x+1) - 2^(x-1)) / 2^x",
                options = listOf("1", "3/2", "2", "1/2"),
                correctAnswerIndex = 1,
                explanation = "Factor out 2^x: 2^x(2 - 1/2) / 2^x = 3/2.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m7",
                subject = "Mathematics",
                topic = "Sequences & Series",
                concept = "Geometric Series",
                questionText = "Calculate the sum to infinity of 16 + 8 + 4 + ...",
                options = listOf("32", "24", "64", "Infinite"),
                correctAnswerIndex = 0,
                explanation = "S_inf = a / (1 - r) = 16 / (1 - 0.5) = 32.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "m8",
                subject = "Mathematics",
                topic = "Functions",
                concept = "Hyperbola",
                questionText = "Find the asymptotes of f(x) = 3 / (x - 2) + 4",
                options = listOf("x = 2, y = 4", "x = -2, y = 4", "x = 4, y = 2", "x = 3, y = 2"),
                correctAnswerIndex = 0,
                explanation = "Vertical asymptote is x = 2, horizontal asymptote is y = 4.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "m9",
                subject = "Mathematics",
                topic = "Analytical Geometry",
                concept = "Distance & Gradient",
                questionText = "Find the gradient of a line perpendicular to 2x + 4y = 8.",
                options = listOf("-1/2", "2", "-2", "1/2"),
                correctAnswerIndex = 1,
                explanation = "The line is y = -1/2x + 2, so m1 = -1/2. Perpendicular gradient m2 = -1 / (-1/2) = 2.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m10",
                subject = "Mathematics",
                topic = "Trigonometry",
                concept = "Sine Rule",
                questionText = "In triangle ABC, a = 10, A = 30°, B = 45°. Find side b.",
                options = listOf("10√2", "5√2", "10 / √2", "14.14"),
                correctAnswerIndex = 0,
                explanation = "b / sin(45°) = a / sin(30°) => b = 10 * (√2 / 2) / 0.5 = 10√2.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m11",
                subject = "Mathematics",
                topic = "Calculus",
                concept = "Optimization",
                questionText = "A particle moves with position s(t) = -t³ + 6t² + 2. When is its acceleration zero?",
                options = listOf("t = 1s", "t = 2s", "t = 3s", "t = 4s"),
                correctAnswerIndex = 1,
                explanation = "v(t) = s'(t) = -3t² + 12t. a(t) = v'(t) = -6t + 12. a(t) = 0 => t = 2s.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "m12",
                subject = "Mathematics",
                topic = "Probability",
                concept = "Venn Diagrams",
                questionText = "If P(A) = 0.6, P(B) = 0.5, and P(A ∩ B) = 0.3, find P(A ∪ B).",
                options = listOf("0.8", "0.9", "1.1", "0.7"),
                correctAnswerIndex = 0,
                explanation = "P(A ∪ B) = P(A) + P(B) - P(A ∩ B) = 0.6 + 0.5 - 0.3 = 0.8.",
                priority = ConceptPriority.LOW
            )
        )
        add(
            Question(
                id = "m13",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Inequalities",
                questionText = "Solve for x: x² - 4x - 5 > 0",
                options = listOf("x < -1 or x > 5", "-1 < x < 5", "x > 5 only", "x < -1 only"),
                correctAnswerIndex = 0,
                explanation = "(x - 5)(x + 1) > 0. The parabola opens upward, so x < -1 or x > 5.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m14",
                subject = "Mathematics",
                topic = "Financial Maths",
                concept = "Compound Interest",
                questionText = "An investment doubles in 6 years compounded annually. What is the approximate interest rate?",
                options = listOf("12.25%", "8.5%", "15.0%", "10.0%"),
                correctAnswerIndex = 0,
                explanation = "(1 + i)^6 = 2 => 1 + i = 2^(1/6) ≈ 1.1225 => i ≈ 12.25%.",
                priority = ConceptPriority.LOW
            )
        )
        add(
            Question(
                id = "m15",
                subject = "Mathematics",
                topic = "Statistics",
                concept = "Standard Deviation",
                questionText = "If every value in a dataset is multiplied by 3, what happens to the standard deviation?",
                options = listOf("Multiplied by 9", "Multiplied by 3", "Unchanged", "Increased by 3"),
                correctAnswerIndex = 1,
                explanation = "Standard deviation scales linearly with multiplication by a constant, so it is multiplied by 3.",
                priority = ConceptPriority.LOW
            )
        )
        add(
            Question(
                id = "m16",
                subject = "Mathematics",
                topic = "Euclidean Geometry",
                concept = "Circle Theorems",
                questionText = "The angle subtended by an arc at the center is ____ that subtended at the circumference.",
                options = listOf("Equal to", "Half of", "Twice", "Triple"),
                correctAnswerIndex = 2,
                explanation = "By circle geometry theorem, angle at center is twice the angle at the circumference.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m17",
                subject = "Mathematics",
                topic = "Trigonometry",
                concept = "Double Angle",
                questionText = "Express cos(2θ) exclusively in terms of sin(θ).",
                options = listOf("1 - 2sin²(θ)", "2sin²(θ) - 1", "cos²(θ) - sin²(θ)", "1 - sin²(θ)"),
                correctAnswerIndex = 0,
                explanation = "cos(2θ) = cos²(θ) - sin²(θ) = (1 - sin²(θ)) - sin²(θ) = 1 - 2sin²(θ).",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "m18",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Simultaneous Equations",
                questionText = "Solve for x and y: y = 2x - 1 and x² + y² = 5 (with x > 0)",
                options = listOf("x = 2, y = 3", "x = 1, y = 1", "x = 3, y = 5", "x = 2, y = -1"),
                correctAnswerIndex = 0,
                explanation = "x² + (2x - 1)² = 5 => 5x² - 4x - 4 = 0 => (5x + 2)(x - 2) = 0. Since x > 0, x = 2, y = 3.",
                priority = ConceptPriority.HIGH
            )
        )

        // Physical Sciences (14 questions)
        add(
            Question(
                id = "p1",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Newton's laws",
                questionText = "A 5kg box rests on a frictionless surface. A 20N force acts horizontally. What is its acceleration?",
                options = listOf("4 m/s²", "100 m/s²", "0.25 m/s²", "2 m/s²"),
                correctAnswerIndex = 0,
                explanation = "Newton's 2nd Law: F_net = m·a => a = F/m = 20 / 5 = 4 m/s².",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p2",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Newton's laws",
                questionText = "State Newton's First Law of Motion.",
                options = listOf(
                    "An object continues at rest or uniform velocity unless acted upon by a net external force",
                    "F_net = m·a",
                    "For every action there is an equal and opposite reaction",
                    "Acceleration is directly proportional to mass"
                ),
                correctAnswerIndex = 0,
                explanation = "Newton's First Law describes the law of inertia.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p3",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Organic chemistry",
                questionText = "Which homologous series does 2-methylbut-2-ene belong to?",
                options = listOf("Alkanes", "Alkenes", "Alkynes", "Haloalkanes"),
                correctAnswerIndex = 1,
                explanation = "The '-ene' suffix designates an alkene containing a carbon-carbon double bond.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p4",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Reaction Rates",
                questionText = "How does increasing temperature increase the rate of a chemical reaction?",
                options = listOf(
                    "Decreases activation energy",
                    "Increases kinetic energy and fraction of particles with E >= E_a",
                    "Reduces concentration of reactants",
                    "Changes the equilibrium constant"
                ),
                correctAnswerIndex = 1,
                explanation = "Higher temperature increases average kinetic energy, causing more collisions with energy >= activation energy.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p5",
                subject = "Physical Sciences",
                topic = "Work, Energy & Power",
                concept = "Work-Energy Theorem",
                questionText = "Work done by a net force on an object is equal to:",
                options = listOf("Change in potential energy", "Change in kinetic energy", "Total mechanical energy", "Rate of power output"),
                correctAnswerIndex = 1,
                explanation = "W_net = ΔE_k = 1/2 m v_f² - 1/2 m v_i².",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "p6",
                subject = "Physical Sciences",
                topic = "Doppler Effect",
                concept = "Sound Waves",
                questionText = "As an ambulance with siren sounding approaches a stationary observer, the observed frequency is:",
                options = listOf("Higher than emitted frequency", "Lower than emitted frequency", "Equal to emitted frequency", "Zero"),
                correctAnswerIndex = 0,
                explanation = "Due to compression of wavefronts ahead of the moving source, observed frequency f_L > f_s.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "p7",
                subject = "Physical Sciences",
                topic = "Electricity & Magnetism",
                concept = "Coulomb's Law",
                questionText = "If the distance between two point charges is halved, the electrostatic force between them is:",
                options = listOf("Halved", "Doubled", "Quadrupled", "Quartered"),
                correctAnswerIndex = 2,
                explanation = "F = k(q1·q2)/r². When r -> r/2, F -> F / (1/4) = 4F.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p8",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Chemical Equilibrium",
                questionText = "For exothermic reaction 2SO₂(g) + O₂(g) ⇌ 2SO₃(g) + Heat, what happens if temperature is raised?",
                options = listOf(
                    "Equilibrium shifts to the right",
                    "Equilibrium shifts to the left (favors reverse endothermic reaction)",
                    "No shift",
                    "K_c increases"
                ),
                correctAnswerIndex = 1,
                explanation = "According to Le Chatelier's Principle, adding heat shifts an exothermic reaction towards the left.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p9",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Organic chemistry",
                questionText = "What is the IUPAC name for CH₃-CH(OH)-CH₂-CH₃?",
                options = listOf("Butan-1-ol", "Butan-2-ol", "2-methylpropan-1-ol", "Butanal"),
                correctAnswerIndex = 1,
                explanation = "4-carbon chain with hydroxyl group at position 2 is Butan-2-ol.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p10",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Vertical Projectile",
                questionText = "A ball thrown upwards reaches max height in 3s. Its initial velocity (g = 9.8 m/s²) is:",
                options = listOf("29.4 m/s", "14.7 m/s", "9.8 m/s", "44.1 m/s"),
                correctAnswerIndex = 0,
                explanation = "v_f = v_i - g·t => 0 = v_i - 9.8(3) => v_i = 29.4 m/s.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p11",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Acids and Bases",
                questionText = "Calculate the pH of a 0.01 mol/dm³ solution of HCl.",
                options = listOf("1", "2", "7", "12"),
                correctAnswerIndex = 1,
                explanation = "pH = -log[H+] = -log(10^-2) = 2.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p12",
                subject = "Physical Sciences",
                topic = "Electricity & Magnetism",
                concept = "Internal Resistance",
                questionText = "A battery with emf 12V and internal resistance 1Ω is connected to a 5Ω resistor. The terminal potential difference is:",
                options = listOf("10V", "12V", "2V", "6V"),
                correctAnswerIndex = 0,
                explanation = "I = 12 / (5 + 1) = 2A. V_term = I * R_ext = 2 * 5 = 10V (or 12 - 2(1) = 10V).",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "p13",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Momentum & Impulse",
                questionText = "An isolated system of colliding bodies always conserves total:",
                options = listOf("Kinetic energy", "Mechanical energy", "Linear momentum", "Velocity"),
                correctAnswerIndex = 2,
                explanation = "In any closed/isolated system, total linear momentum is strictly conserved.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "p14",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Galvanic Cells",
                questionText = "At which electrode does oxidation occur in an electrochemical cell?",
                options = listOf("Anode", "Cathode", "Salt bridge", "Electrolyte solution"),
                correctAnswerIndex = 0,
                explanation = "An Ox / Red Cat: Oxidation occurs at the Anode, Reduction at the Cathode.",
                priority = ConceptPriority.HIGH
            )
        )

        // English (10 questions)
        add(
            Question(
                id = "e1",
                subject = "English",
                topic = "Grammar",
                concept = "Direct/Indirect Speech",
                questionText = "Convert to indirect speech: 'I will finish the exam today,' John promised.",
                options = listOf(
                    "John promised that he would finish the exam that day.",
                    "John promised that I will finish the exam today.",
                    "John promised that he will finish the exam today.",
                    "John promised he would finish the exam tomorrow."
                ),
                correctAnswerIndex = 0,
                explanation = "'will' shifts to 'would' and 'today' shifts to 'that day'.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "e2",
                subject = "English",
                topic = "Literary Analysis",
                concept = "Literary Devices",
                questionText = "'The wind wailed like a lonely child.' This line contains an example of:",
                options = listOf("Metaphor and Oxymoron", "Simile and Personification", "Hyperbole and Irony", "Alliteration and Pun"),
                correctAnswerIndex = 1,
                explanation = "'like a lonely child' is a simile; attributing crying/wailing to wind is personification.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "e3",
                subject = "English",
                topic = "Comprehension",
                concept = "Critical Analysis",
                questionText = "What tone is reflected when an author uses sarcastic understatements to critique corporate greed?",
                options = listOf("Nostalgic", "Satirical", "Objective", "Melancholic"),
                correctAnswerIndex = 1,
                explanation = "Satire uses irony, humor, or sarcasm to expose and criticize societal vices.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "e4",
                subject = "English",
                topic = "Vocabulary",
                concept = "Contextual Meaning",
                questionText = "In the sentence 'Her argument was cogent and persuaded the panel', 'cogent' means:",
                options = listOf("Convoluted", "Compelling and clear", "Aggressive", "Unsubstantiated"),
                correctAnswerIndex = 1,
                explanation = "Cogent means powerfully convincing, logical, and lucid.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "e5",
                subject = "English",
                topic = "Grammar",
                concept = "Passive Voice",
                questionText = "Identify the correct passive voice: 'The committee approved the budget.'",
                options = listOf(
                    "The budget was approved by the committee.",
                    "The budget is approved by the committee.",
                    "The committee has approved the budget.",
                    "The budget had been approved."
                ),
                correctAnswerIndex = 0,
                explanation = "Simple past active becomes 'was/were + past participle'.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "e6",
                subject = "English",
                topic = "Literary Analysis",
                concept = "Poetic Form",
                questionText = "A fourteen-line poem written in iambic pentameter with an ABAB CDCD EFEF GG rhyme scheme is an:",
                options = listOf("Italian/Petrarchan Sonnet", "English/Shakespearean Sonnet", "Ode", "Elegy"),
                correctAnswerIndex = 1,
                explanation = "Three quatrains and a rhyming couplet is the Shakespearean sonnet structure.",
                priority = ConceptPriority.MEDIUM
            )
        )
        add(
            Question(
                id = "e7",
                subject = "English",
                topic = "Grammar",
                concept = "Subject-Verb Agreement",
                questionText = "Select the grammatically correct sentence:",
                options = listOf(
                    "Neither of the boys were present.",
                    "Neither of the boys was present.",
                    "Neither of the boys are present.",
                    "Neither of the boy was present."
                ),
                correctAnswerIndex = 1,
                explanation = "'Neither' takes a singular verb: 'was present'.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "e8",
                subject = "English",
                topic = "Summary Writing",
                concept = "Synthesis",
                questionText = "When summarizing a passage, a student must strictly:",
                options = listOf(
                    "Copy verbatim sentences from the text",
                    "Include personal opinions and emotions",
                    "Paraphrase key points within the word limit",
                    "Expand with external examples"
                ),
                correctAnswerIndex = 2,
                explanation = "Summary writing requires concise synthesis in the student's own words.",
                priority = ConceptPriority.HIGH
            )
        )
        add(
            Question(
                id = "e9",
                subject = "English",
                topic = "Vocabulary",
                concept = "Idioms",
                questionText = "What does the idiom 'to burn the midnight oil' mean?",
                options = listOf("To waste resources", "To work late into the night", "To start a conflict", "To sleep deeply"),
                correctAnswerIndex = 1,
                explanation = "It means studying or working late into the night.",
                priority = ConceptPriority.LOW
            )
        )
        add(
            Question(
                id = "e10",
                subject = "English",
                topic = "Critical Reading",
                concept = "Fact vs Opinion",
                questionText = "Which of the following statements represents an objective fact?",
                options = listOf(
                    "Mathematics is the most fascinating subject.",
                    "The periodic table categorizes elements by atomic number.",
                    "Literature is superior to science.",
                    "Examinations are needlessly stressful."
                ),
                correctAnswerIndex = 1,
                explanation = "Atomic number categorization is an empirically verifiable scientific fact.",
                priority = ConceptPriority.MEDIUM
            )
        )
    }

    // State Holders
    private val _questions = MutableStateFlow(rawQuestions)
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _examScans = MutableStateFlow<List<ExamScan>>(
        listOf(
            ExamScan(
                id = "scan-1",
                title = "Matric Trial Paper - Math & Physics",
                subject = "Mathematics, Physical Sciences, English",
                dateScanned = "Sept 27, 2026",
                detectedQuestionsCount = 42,
                detectedConceptsCount = 27,
                score = 86,
                status = "COMPLETED",
                summary = "High accuracy in Algebra & Kinematics. Review recommended for Quadratics, Newton's Laws, and Organic Chemistry nomenclature.",
                highPriorityFocus = listOf("Algebra", "Trigonometry", "Chemical reactions", "Forces & motion"),
                reviewAreas = listOf("Quadratics", "Newton's laws", "Organic chemistry")
            ),
            ExamScan(
                id = "scan-2",
                title = "Cambridge AS Level Mock Exam",
                subject = "Physical Sciences",
                dateScanned = "Sept 20, 2026",
                detectedQuestionsCount = 28,
                detectedConceptsCount = 19,
                score = 79,
                status = "COMPLETED",
                summary = "Strong grasp of electrostatics and equilibrium. Work required on galvanic half-cells.",
                highPriorityFocus = listOf("Forces & motion", "Chemical reactions"),
                reviewAreas = listOf("Galvanic Cells", "Vertical Projectiles")
            )
        )
    )
    val examScans: StateFlow<List<ExamScan>> = _examScans.asStateFlow()

    private val _mistakes = MutableStateFlow<List<MistakeReviewItem>>(
        listOf(
            MistakeReviewItem(
                id = "m-1",
                questionId = "m2",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Quadratics",
                questionText = "Determine the nature of the roots for the equation 3x² + 4x + 5 = 0.",
                yourAnswer = "Real and equal",
                correctAnswer = "Non-real (complex)",
                explanation = "Discriminant Δ = b² - 4ac = 16 - 60 = -44 < 0. Negative discriminant means non-real complex conjugate roots.",
                priority = ConceptPriority.HIGH
            ),
            MistakeReviewItem(
                id = "m-2",
                questionId = "p1",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Newton's laws",
                questionText = "A 5kg box rests on a frictionless surface. A 20N force acts horizontally. What is its acceleration?",
                yourAnswer = "100 m/s²",
                correctAnswer = "4 m/s²",
                explanation = "F_net = m·a => a = F/m = 20 / 5 = 4 m/s². Do not multiply mass by force.",
                priority = ConceptPriority.HIGH
            ),
            MistakeReviewItem(
                id = "m-3",
                questionId = "p3",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Organic chemistry",
                questionText = "Which homologous series does 2-methylbut-2-ene belong to?",
                yourAnswer = "Alkanes",
                correctAnswer = "Alkenes",
                explanation = "The suffix '-ene' indicates a carbon=carbon double bond belonging to the alkene family.",
                priority = ConceptPriority.HIGH
            ),
            MistakeReviewItem(
                id = "m-4",
                questionId = "e1",
                subject = "English",
                topic = "Grammar",
                concept = "Direct/Indirect Speech",
                questionText = "Convert to indirect speech: 'I will finish the exam today,' John promised.",
                yourAnswer = "John promised that he will finish the exam today.",
                correctAnswer = "John promised that he would finish the exam that day.",
                explanation = "Reported speech shifts back: 'will' becomes 'would', and 'today' becomes 'that day'.",
                priority = ConceptPriority.HIGH
            )
        )
    )
    val mistakes: StateFlow<List<MistakeReviewItem>> = _mistakes.asStateFlow()

    private val _studyPlans = MutableStateFlow<List<StudyPlanItem>>(
        listOf(
            StudyPlanItem(
                id = "sp-1",
                subject = "Mathematics",
                topic = "Algebra",
                concept = "Quadratics",
                priority = ConceptPriority.HIGH,
                hoursRecommended = 3.5,
                status = PlanStatus.IN_PROGRESS,
                targetDate = "Oct 02, 2026",
                notionPageId = "notion.so/beast/quadratics-mastery"
            ),
            StudyPlanItem(
                id = "sp-2",
                subject = "Physical Sciences",
                topic = "Forces & motion",
                concept = "Newton's laws",
                priority = ConceptPriority.HIGH,
                hoursRecommended = 4.0,
                status = PlanStatus.TODO,
                targetDate = "Oct 04, 2026",
                notionPageId = "notion.so/beast/newton-mechanics"
            ),
            StudyPlanItem(
                id = "sp-3",
                subject = "Physical Sciences",
                topic = "Chemical reactions",
                concept = "Organic chemistry",
                priority = ConceptPriority.HIGH,
                hoursRecommended = 3.0,
                status = PlanStatus.TODO,
                targetDate = "Oct 06, 2026",
                notionPageId = "notion.so/beast/organic-nomenclature"
            ),
            StudyPlanItem(
                id = "sp-4",
                subject = "Mathematics",
                topic = "Trigonometry",
                concept = "Reduction & Identities",
                priority = ConceptPriority.HIGH,
                hoursRecommended = 2.5,
                status = PlanStatus.COMPLETED,
                targetDate = "Sept 28, 2026",
                notionPageId = "notion.so/beast/trig-identities"
            )
        )
    )
    val studyPlans: StateFlow<List<StudyPlanItem>> = _studyPlans.asStateFlow()

    // -----------------------------------------------------------------------
    // TRADING ENGINE (EA NEXUS)
    // -----------------------------------------------------------------------

    private val _robots = MutableStateFlow<List<RobotEA>>(
        listOf(
            RobotEA(
                id = "ea-1",
                name = "WAKANDA WEALTH EA",
                mentor = "Thesia",
                status = RobotStatus.ACTIVE,
                accountBound = "MT5 #8849201 - Deriv SVG",
                winRate = 88.5,
                totalProfit = 14820.50,
                activeTrades = 2,
                licenseKey = "BEAST-9921-ACTIVE-PRO"
            ),
            RobotEA(
                id = "ea-2",
                name = "THE BEAST SCALPER V2",
                mentor = "Thesia",
                status = RobotStatus.STANDBY,
                accountBound = "MT4 #4412903 - ICMarkets",
                winRate = 92.1,
                totalProfit = 8450.00,
                activeTrades = 0,
                licenseKey = "BEAST-7740-VIP"
            )
        )
    )
    val robots: StateFlow<List<RobotEA>> = _robots.asStateFlow()

    // Valid license keys store
    val validLicenses = mutableMapOf(
        "BEAST-9921-ACTIVE-PRO" to LicenseKey(
            key = "BEAST-9921-ACTIVE-PRO",
            isActive = true,
            isExpired = false,
            planName = "Enterprise Algo Pro",
            expirationDate = "Dec 31, 2027",
            boundAccounts = listOf("MT5 #8849201")
        ),
        "BEAST-7740-VIP" to LicenseKey(
            key = "BEAST-7740-VIP",
            isActive = true,
            isExpired = false,
            planName = "VIP Scalper Lifetime",
            expirationDate = "Lifetime Access",
            boundAccounts = listOf("MT4 #4412903")
        ),
        "BEAST-EXPIRED-TEST" to LicenseKey(
            key = "BEAST-EXPIRED-TEST",
            isActive = true,
            isExpired = true,
            planName = "Trial Monthly",
            expirationDate = "Aug 15, 2026",
            boundAccounts = emptyList()
        ),
        "BEAST-INACTIVE-TEST" to LicenseKey(
            key = "BEAST-INACTIVE-TEST",
            isActive = false,
            isExpired = false,
            planName = "Revoked Key",
            expirationDate = "Nov 10, 2026",
            boundAccounts = emptyList()
        )
    )

    private val _commands = MutableStateFlow<List<TradeCommand>>(
        listOf(
            TradeCommand(
                id = "cmd-1",
                symbol = "XAUUSD",
                type = CommandType.BUY,
                lotSize = 0.01,
                status = CommandStatus.EXECUTED,
                timestamp = System.currentTimeMillis() - 120000,
                executionPrice = 2652.40,
                profitLoss = 42.50,
                note = "Neural order block bounce"
            ),
            TradeCommand(
                id = "cmd-2",
                symbol = "BTCUSD",
                type = CommandType.BUY,
                lotSize = 0.02,
                status = CommandStatus.EXECUTED,
                timestamp = System.currentTimeMillis() - 480000,
                executionPrice = 64200.0,
                profitLoss = 88.00,
                note = "Liquidity sweep trigger"
            ),
            TradeCommand(
                id = "cmd-3",
                symbol = "NAS100",
                type = CommandType.START_AUTO,
                lotSize = 0.05,
                status = CommandStatus.RECEIVED,
                timestamp = System.currentTimeMillis() - 60000,
                executionPrice = 20120.0,
                profitLoss = 0.0,
                note = "Auto-EA session activated"
            )
        )
    )
    val commands: StateFlow<List<TradeCommand>> = _commands.asStateFlow()

    private val _openPositions = MutableStateFlow<List<OpenPosition>>(
        listOf(
            OpenPosition("pos-1", "XAUUSD", CommandType.BUY, 0.01, 2652.40, 2656.65, 42.50, "10:34 UTC"),
            OpenPosition("pos-2", "BTCUSD", CommandType.BUY, 0.02, 64200.0, 64640.0, 88.00, "10:28 UTC")
        )
    )
    val openPositions: StateFlow<List<OpenPosition>> = _openPositions.asStateFlow()

    val marketSessions = listOf(
        MarketSession("London", "UTC+0", SessionStatus.OPEN, "08:00 - 16:00 UTC", "HIGH (Gold & GBP)"),
        MarketSession("New York", "UTC-4", SessionStatus.OPEN, "13:00 - 22:00 UTC", "MAXIMUM (Indices & Metals)"),
        MarketSession("Asian / Tokyo", "UTC+9", SessionStatus.CLOSED, "00:00 - 09:00 UTC", "MODERATE (JPY & AUD)"),
        MarketSession("Sydney", "UTC+10", SessionStatus.CLOSED, "21:00 - 06:00 UTC", "LOW (Consolidation)")
    )

    // License Validation Logic strictly conforming to prompt:
    // LICENSE KEY -> Validate key -> ACTIVE? NO -> REJECT; YES -> EXPIRED? YES -> REJECT; NO -> ALLOW -> Bind to MT4/MT5 account -> EA ACTIVATED
    sealed class LicenseValidationResult {
        data class Success(val key: LicenseKey, val accountBound: String) : LicenseValidationResult()
        data class Rejected(val reason: String) : LicenseValidationResult()
    }

    fun validateAndBindLicense(keyInput: String, targetAccount: String): LicenseValidationResult {
        val trimmed = keyInput.trim()
        val lic = validLicenses[trimmed]
            ?: return LicenseValidationResult.Rejected("Invalid License Key. Key not found in Beast records.")

        if (!lic.isActive) {
            return LicenseValidationResult.Rejected("REJECTED: License Key is INACTIVE or revoked.")
        }

        if (lic.isExpired) {
            return LicenseValidationResult.Rejected("REJECTED: License Key is EXPIRED on ${lic.expirationDate}.")
        }

        // Key is active and not expired: ALLOW & bind to MT4/MT5 account -> EA ACTIVATED
        val updatedBound = (lic.boundAccounts + targetAccount).distinct()
        validLicenses[trimmed] = lic.copy(boundAccounts = updatedBound)

        val newRobot = RobotEA(
            id = "ea-${UUID.randomUUID().toString().take(6)}",
            name = "BEAST ALGO BOT",
            mentor = "Thesia",
            status = RobotStatus.ACTIVE,
            accountBound = targetAccount,
            winRate = 89.2,
            totalProfit = 0.0,
            activeTrades = 0,
            licenseKey = trimmed
        )
        _robots.value = _robots.value + newRobot

        return LicenseValidationResult.Success(lic, targetAccount)
    }

    // Command lifecycle: PENDING -> RECEIVED -> EXECUTED or REJECTED
    fun dispatchCommand(symbol: String, type: CommandType, lotSize: Double, executionPrice: Double, onStatusUpdate: ((TradeCommand) -> Unit)? = null): TradeCommand {
        val commandId = "cmd-${UUID.randomUUID().toString().take(6)}"
        val initialCmd = TradeCommand(
            id = commandId,
            symbol = symbol,
            type = type,
            lotSize = lotSize,
            status = CommandStatus.PENDING,
            timestamp = System.currentTimeMillis(),
            executionPrice = executionPrice,
            note = "Order sent to MT5 Bridge"
        )
        _commands.value = listOf(initialCmd) + _commands.value
        onStatusUpdate?.invoke(initialCmd)
        return initialCmd
    }

    fun updateCommandStatus(commandId: String, newStatus: CommandStatus, profitLoss: Double = 0.0) {
        _commands.value = _commands.value.map { cmd ->
            if (cmd.id == commandId) {
                cmd.copy(status = newStatus, profitLoss = profitLoss)
            } else cmd
        }
    }

    fun toggleRobot(robotId: String) {
        _robots.value = _robots.value.map { r ->
            if (r.id == robotId) {
                val nextStatus = when (r.status) {
                    RobotStatus.ACTIVE -> RobotStatus.PAUSED
                    RobotStatus.PAUSED -> RobotStatus.ACTIVE
                    RobotStatus.STANDBY -> RobotStatus.ACTIVE
                }
                r.copy(status = nextStatus)
            } else r
        }
    }

    fun removeRobot(robotId: String) {
        _robots.value = _robots.value.filterNot { it.id == robotId }
    }

    fun addExamScan(scan: ExamScan) {
        _examScans.value = listOf(scan) + _examScans.value
    }

    fun markPlanCompleted(planId: String) {
        _studyPlans.value = _studyPlans.value.map { p ->
            if (p.id == planId) p.copy(status = PlanStatus.COMPLETED) else p
        }
    }

    fun updateQuestionAnswer(questionId: String, selectedOption: Int) {
        _questions.value = _questions.value.map { q ->
            if (q.id == questionId) q.copy(userSelectedOption = selectedOption) else q
        }
    }
}
