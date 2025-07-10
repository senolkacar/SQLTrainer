package com.senolkacar.sqltrainer.service;

import com.senolkacar.sqltrainer.entity.*;
import com.senolkacar.sqltrainer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired private TeacherRepository teacherRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private DatabaseRepository databaseRepository;
    @Autowired private QuizRepository quizRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private SolutionRepository solutionRepository;
    @Autowired private AttemptRepository attemptRepository;
    @Autowired private AnswerRepository answerRepository;

    @Override
    public void run(String... args) {
        if (teacherRepository.count() == 0 && studentRepository.count() == 0) {
            createUsers();
            createDatabases();
            createQuizzes();
            createQuestionsQuiz1();
            createQuestionsQuiz2();
            createQuestionsQuiz3();
            createQuestionsQuiz4();
            createQuestionsQuiz5();
            createQuestionsQuiz6();
            createAttempts();
        }
    }

    private void createUsers() {
        // Teachers
        Teacher ben = new Teacher();
        ben.setId(2L);
        ben.setPseudo("ben");
        ben.setPassword("ben");
        ben.setFirstName("Benoît");
        ben.setLastName("Penelle");
        ben.setEmail("penelle@epfc.eu");
        teacherRepository.save(ben);

        Teacher bruno = new Teacher();
        bruno.setId(3L);
        bruno.setPseudo("bruno");
        bruno.setPassword("bruno");
        bruno.setFirstName("Bruno");
        bruno.setLastName("Lacroix");
        bruno.setEmail("lacroix@epfc.eu");
        teacherRepository.save(bruno);

        // Students
        Student bob = new Student();
        bob.setId(4L);
        bob.setPseudo("bob");
        bob.setPassword("bob");
        bob.setFirstName("Bob");
        bob.setLastName("l'Eponge");
        bob.setEmail("bob@epfc.eu");
        studentRepository.save(bob);

        Student caro = new Student();
        caro.setId(5L);
        caro.setPseudo("caro");
        caro.setPassword("caro");
        caro.setFirstName("Caroline");
        caro.setLastName("de Monaco");
        caro.setEmail("caro@epfc.eu");
        studentRepository.save(caro);
    }

    private void createDatabases() {
        Database fournisseurs = new Database();
        fournisseurs.setId(1L);
        fournisseurs.setName("fournisseurs");
        fournisseurs.setDescription("Base de données fournisseurs");
        databaseRepository.save(fournisseurs);

        Database facebook = new Database();
        facebook.setId(2L);
        facebook.setName("facebook");
        facebook.setDescription("Base de données facebook");
        databaseRepository.save(facebook);
    }

    private void createQuizzes() {
        Database fournisseurs = databaseRepository.findById(1L).orElseThrow();
        Database facebook = databaseRepository.findById(2L).orElseThrow();

        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);
        quiz1.setName("TP1");
        quiz1.setDatabase(fournisseurs);
        quiz1.setPublished(true);
        quizRepository.save(quiz1);

        Quiz quiz2 = new Quiz();
        quiz2.setId(2L);
        quiz2.setName("TP2");
        quiz2.setDatabase(fournisseurs);
        quiz2.setPublished(true);
        quizRepository.save(quiz2);

        Quiz quiz3 = new Quiz();
        quiz3.setId(3L);
        quiz3.setName("TP4");
        quiz3.setDatabase(facebook);
        quiz3.setPublished(true);
        quizRepository.save(quiz3);

        Quiz quiz4 = new Quiz();
        quiz4.setId(4L);
        quiz4.setName("TEST2");
        quiz4.setDatabase(fournisseurs);
        quiz4.setPublished(true);
        quiz4.setTest(true);
        quiz4.setStartDate(OffsetDateTime.now().minusDays(1));
        quiz4.setEndDate(OffsetDateTime.now().plusDays(1));
        quizRepository.save(quiz4);

        Quiz quiz5 = new Quiz();
        quiz5.setId(5L);
        quiz5.setName("TEST1");
        quiz5.setDatabase(fournisseurs);
        quiz5.setPublished(true);
        quiz5.setTest(true);
        quiz5.setStartDate(OffsetDateTime.now().minusDays(2));
        quiz5.setEndDate(OffsetDateTime.now().minusDays(1));
        quizRepository.save(quiz5);

        Quiz quiz6 = new Quiz();
        quiz6.setId(6L);
        quiz6.setName("TEST3");
        quiz6.setDatabase(facebook);
        quiz6.setPublished(true);
        quiz6.setTest(true);
        quiz6.setStartDate(OffsetDateTime.now());
        quiz6.setEndDate(OffsetDateTime.now().plusDays(2));
        quizRepository.save(quiz6);
    }

    private void createQuestionsQuiz1() {
        Quiz quiz1 = quizRepository.findById(1L).orElseThrow();

        // Questions
        Question q1 = new Question();
        q1.setId(1L);
        q1.setQuiz(quiz1);
        q1.setOrder(1);
        q1.setBody("On veut afficher le contenu de la table des fournisseurs.");
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setId(2L);
        q2.setQuiz(quiz1);
        q2.setOrder(2);
        q2.setBody("On veut le nom et la ville de tous les fournisseurs avec comme intitulé NOM et VILLE.");
        questionRepository.save(q2);

        Question q3 = new Question();
        q3.setId(3L);
        q3.setQuiz(quiz1);
        q3.setOrder(3);
        q3.setBody("On veut le nom des fournisseurs dont la ville est London ou Paris.");
        questionRepository.save(q3);

        Question q4 = new Question();
        q4.setId(4L);
        q4.setQuiz(quiz1);
        q4.setOrder(4);
        q4.setBody("On veut le nom des fournisseurs dont le statut vaut strictement moins de 25 et qui sont de Paris.");
        questionRepository.save(q4);

        Question q5 = new Question();
        q5.setId(5L);
        q5.setQuiz(quiz1);
        q5.setOrder(5);
        q5.setBody("On veut obtenir le nom des fournisseurs dont le statut n'est pas dans l'intervalle fermé [15,25]. Autrement dit, le statut doit être strictement inférieur à 15 ou strictement supérieur à 25.");
        questionRepository.save(q5);

        Question q6 = new Question();
        q6.setId(6L);
        q6.setQuiz(quiz1);
        q6.setOrder(6);
        q6.setBody("On veut obtenir les noms des pièces rouges ou bleues. On ne veut pas de doublon.");
        questionRepository.save(q6);

        // Solutions
        Solution s1 = new Solution();
        s1.setId(1L);
        s1.setQuestion(q1);
        s1.setOrder(1);
        s1.setSql("SELECT * FROM s");
        solutionRepository.save(s1);

        Solution s2 = new Solution();
        s2.setId(2L);
        s2.setQuestion(q2);
        s2.setOrder(1);
        s2.setSql("SELECT sname NOM, city VILLE FROM s");
        solutionRepository.save(s2);

        Solution s3 = new Solution();
        s3.setId(3L);
        s3.setQuestion(q3);
        s3.setOrder(1);
        s3.setSql("SELECT sname\nFROM s\nWHERE city='London' OR city='Paris'");
        solutionRepository.save(s3);

        Solution s4 = new Solution();
        s4.setId(4L);
        s4.setQuestion(q3);
        s4.setOrder(2);
        s4.setSql("SELECT sname\nFROM s\nWHERE city IN ('London', 'Paris')");
        solutionRepository.save(s4);

        Solution s5 = new Solution();
        s5.setId(5L);
        s5.setQuestion(q4);
        s5.setOrder(1);
        s5.setSql("SELECT sname\nFROM s\nWHERE status < 25 AND city='Paris'");
        solutionRepository.save(s5);

        Solution s6 = new Solution();
        s6.setId(6L);
        s6.setQuestion(q5);
        s6.setOrder(1);
        s6.setSql("SELECT sname\nFROM s\nWHERE status NOT BETWEEN 15 AND 25");
        solutionRepository.save(s6);

        Solution s7 = new Solution();
        s7.setId(7L);
        s7.setQuestion(q5);
        s7.setOrder(2);
        s7.setSql("SELECT sname\nFROM s\nWHERE status < 15 OR status >25");
        solutionRepository.save(s7);

        Solution s8 = new Solution();
        s8.setId(8L);
        s8.setQuestion(q5);
        s8.setOrder(3);
        s8.setSql("SELECT sname\nFROM s\nWHERE NOT(status >= 15 AND status <= 25)\n-- si on applique de Morgan, on retrouve la solution 2");
        solutionRepository.save(s8);

        Solution s9 = new Solution();
        s9.setId(9L);
        s9.setQuestion(q6);
        s9.setOrder(1);
        s9.setSql("SELECT DISTINCT PNAME FROM p WHERE  Color = 'RED' OR Color = 'BLUE'");
        solutionRepository.save(s9);

        Solution s10 = new Solution();
        s10.setId(10L);
        s10.setQuestion(q6);
        s10.setOrder(2);
        s10.setSql("SELECT DISTINCT PNAME FROM p WHERE Color IN ('RED', 'BLUE')");
        solutionRepository.save(s10);
    }

    private void createQuestionsQuiz2() {
        Quiz quiz2 = quizRepository.findById(2L).orElseThrow();

        // Questions
        Question q7 = new Question();
        q7.setId(7L);
        q7.setQuiz(quiz2);
        q7.setOrder(1);
        q7.setBody("Affichez l'identifiant des livraisons qui concernent un produit rouge.");
        questionRepository.save(q7);

        Question q8 = new Question();
        q8.setId(8L);
        q8.setQuiz(quiz2);
        q8.setOrder(2);
        q8.setBody("Affichez le nom des fournisseurs qui fournissent le produit P4");
        questionRepository.save(q8);

        Question q9 = new Question();
        q9.setId(9L);
        q9.setQuiz(quiz2);
        q9.setOrder(3);
        q9.setBody("Affichez le nom des clients/projets qui utilisent le produit P3");
        questionRepository.save(q9);

        Question q10 = new Question();
        q10.setId(10L);
        q10.setQuiz(quiz2);
        q10.setOrder(4);
        q10.setBody("Affichez le nom des projets fournis par le fournisseur S1");
        questionRepository.save(q10);

        Question q11 = new Question();
        q11.setId(11L);
        q11.setQuiz(quiz2);
        q11.setOrder(5);
        q11.setBody("Affichez le nom des fournisseurs qui ont fait au moins une livraison d'entre 150 et 250 pièces");
        questionRepository.save(q11);

        Question q12 = new Question();
        q12.setId(12L);
        q12.setQuiz(quiz2);
        q12.setOrder(6);
        q12.setBody("On souhaite obtenir l'affichage des livraisons avec le nom du fournisseur à la place de son identifiant, ainsi que le nom de la pièce à la place de son identifiant et le nom du projet à la place de son identifiant.\n\nLa date de dernière livraison ne doit pas figurer dans le résultat.\n\nPar contre l'entête affichée doit être `NOM`, `PIECE`, `PROJET` et `QUANTITE`.\n\nPar exemple : \n\nNOM    PIECE    PROJET    QUANTITE\nSmith  Nut      Sorter    200\nSmith  Nut      Console   700\n…      …        …         …");
        questionRepository.save(q12);

        Question q13 = new Question();
        q13.setId(13L);
        q13.setQuiz(quiz2);
        q13.setOrder(7);
        q13.setBody("Même requête où l'on ne garde que les fournisseurs de Paris");
        questionRepository.save(q13);

        Question q14 = new Question();
        q14.setId(14L);
        q14.setQuiz(quiz2);
        q14.setOrder(8);
        q14.setBody("Affichez les villes des projets où le fournisseur Adams a livré.\nAttention, vous ne pouvez pas faire d'hypothèse sur les données : vous ne pouvez pas considérer que Adams est le fournisseur S5.");
        questionRepository.save(q14);

        Question q15 = new Question();
        q15.setId(15L);
        q15.setQuiz(quiz2);
        q15.setOrder(9);
        q15.setBody("On souhaite le nom des pièces dont le poids est strictement inférieur à 18 livres et qui sont fournies par un fournisseur de Rome ou de Londres.\nMême remarque : vous ne pouvez pas supposer qu'il n'y a pas de fournisseur à Rome.");
        questionRepository.save(q15);

        Question q16 = new Question();
        q16.setId(16L);
        q16.setQuiz(quiz2);
        q16.setOrder(10);
        q16.setBody("Obtenir l'identifiant des pièces de Londres qui ont étés livrées par un fournisseur de Londres");
        questionRepository.save(q16);

        // Solutions for Quiz 2
        Solution s11 = new Solution();
        s11.setId(11L);
        s11.setQuestion(q7);
        s11.setOrder(1);
        s11.setSql("SELECT l.ID_S, l.ID_P, l.ID_J\nFROM SPJ l, P p\nWHERE l.ID_P = p.ID_P AND p.Color = 'Red'");
        solutionRepository.save(s11);

        Solution s12 = new Solution();
        s12.setId(12L);
        s12.setQuestion(q7);
        s12.setOrder(2);
        s12.setSql("-- sans renommage\nSELECT ID_S, SPJ.ID_P, ID_J\nFROM SPJ, P\nWHERE SPJ.ID_P = P.ID_P AND P.Color = 'Red'");
        solutionRepository.save(s12);

        Solution s13 = new Solution();
        s13.setId(13L);
        s13.setQuestion(q8);
        s13.setOrder(1);
        s13.setSql("SELECT DISTINCT s.SNAME\nFROM S s, SPJ l\nWHERE s.ID_S = l.ID_S AND l.ID_P = 'P4'");
        solutionRepository.save(s13);

        Solution s14 = new Solution();
        s14.setId(14L);
        s14.setQuestion(q9);
        s14.setOrder(1);
        s14.setSql("SELECT DISTINCT j.JNAME\nFROM J j, SPJ l\nWHERE j.ID_J = l.ID_J AND l.ID_P = 'P3'");
        solutionRepository.save(s14);

        Solution s15 = new Solution();
        s15.setId(15L);
        s15.setQuestion(q10);
        s15.setOrder(1);
        s15.setSql("SELECT DISTINCT j.JNAME\nFROM J j, SPJ l\nWHERE j.ID_J = l.ID_J AND l.ID_S = 'S1'");
        solutionRepository.save(s15);

        Solution s16 = new Solution();
        s16.setId(16L);
        s16.setQuestion(q11);
        s16.setOrder(1);
        s16.setSql("SELECT DISTINCT S.SNAME\nFROM S, SPJ\nWHERE S.ID_S = SPJ.ID_S AND QTY BETWEEN 150 AND 250");
        solutionRepository.save(s16);

        Solution s17 = new Solution();
        s17.setId(17L);
        s17.setQuestion(q12);
        s17.setOrder(1);
        s17.setSql(" -- On veut :\n -- NOM    PIECE    PROJET    QUANTITE\n -- Smith  Nut     Sorter    200\n -- Smith  Nut      Console   700\n -- ...\n -- au lieu de :\n -- S1 P1 J1 200\n -- S1 P1 J4 700\n -- ...\nSELECT DISTINCT s.SNAME NOM, p.PNAME PIECE, j.JNAME PROJET, l.QTY QUANTITE\nFROM P p, S s, J j, SPJ l\nWHERE l.ID_P = p.ID_P\n  AND l.ID_S = s.ID_S\n  AND l.ID_J = j.ID_J\n -- 4 tables --> 3 jointures\n -- Le distinct est nécessaire pour éviter un doublon dans le cas (peu probable, mais possible)\n -- où on aurait deux livraisons de même quantité pour deux pièces ayant le même nom, deux\n -- fournisseurs ayant le même nom et/ou deux projets ayant le même nom.");
        solutionRepository.save(s17);

        Solution s18 = new Solution();
        s18.setId(18L);
        s18.setQuestion(q13);
        s18.setOrder(1);
        s18.setSql("SELECT DISTINCT s.SNAME NOM, p.PNAME PIECE, j.JNAME PROJET, l.QTY QUANTITE\nFROM P p, S s, J j, SPJ l\nWHERE l.ID_P = p.ID_P\n  AND l.ID_S = s.ID_S\n  AND l.ID_J = j.ID_J\n  AND s.CITY = 'Paris'");
        solutionRepository.save(s18);

        Solution s19 = new Solution();
        s19.setId(19L);
        s19.setQuestion(q14);
        s19.setOrder(1);
        s19.setSql("SELECT DISTINCT j.City\nFROM S s, J j, SPJ l\nWHERE l.ID_S = s.ID_S\n  AND l.ID_J = j.ID_J\n  AND s.SNAME = 'Adams'");
        solutionRepository.save(s19);

        Solution s20 = new Solution();
        s20.setId(20L);
        s20.setQuestion(q15);
        s20.setOrder(1);
        s20.setSql("SELECT DISTINCT p.PNAME\nFROM P p, S s, SPJ l\nWHERE l.ID_P = p.ID_P\n  AND l.ID_S = s.ID_S\n  AND p.WEIGHT < 18\n  AND (s.CITY = 'Rome' OR s.CITY = 'London')");
        solutionRepository.save(s20);

        Solution s21 = new Solution();
        s21.setId(21L);
        s21.setQuestion(q16);
        s21.setOrder(1);
        s21.setSql("SELECT DISTINCT p.ID_P\nFROM P p, S s, SPJ l\nWHERE l.ID_P = p.ID_P\n  AND l.ID_S = s.ID_S\n  AND p.CITY = 'London'\n  AND s.CITY = 'London'");
        solutionRepository.save(s21);
    }

    private void createQuestionsQuiz3() {
        Quiz quiz3 = quizRepository.findById(3L).orElseThrow();

        Question q20 = new Question();
        q20.setId(20L);
        q20.setQuiz(quiz3);
        q20.setOrder(1);
        q20.setBody("Affichez le nom des personnes qui ont plus de trente ans");
        questionRepository.save(q20);

        Solution s22 = new Solution();
        s22.setId(22L);
        s22.setQuestion(q20);
        s22.setOrder(1);
        s22.setSql("SELECT DISTINCT p.Nom\nFROM Personne p\nWHERE p.Age >= 30;");
        solutionRepository.save(s22);
    }

    private void createQuestionsQuiz4() {
        Quiz quiz4 = quizRepository.findById(4L).orElseThrow();

        Question q30 = new Question();
        q30.setId(30L);
        q30.setQuiz(quiz4);
        q30.setOrder(1);
        q30.setBody("On veut afficher le contenu de la table des fournisseurs.");
        questionRepository.save(q30);

        Question q31 = new Question();
        q31.setId(31L);
        q31.setQuiz(quiz4);
        q31.setOrder(2);
        q31.setBody("On veut le nom et la ville de tous les fournisseurs avec comme intitulé NOM et VILLE.");
        questionRepository.save(q31);

        Solution s23 = new Solution();
        s23.setId(23L);
        s23.setQuestion(q30);
        s23.setOrder(1);
        s23.setSql("SELECT * FROM s");
        solutionRepository.save(s23);

        Solution s24 = new Solution();
        s24.setId(24L);
        s24.setQuestion(q31);
        s24.setOrder(1);
        s24.setSql("SELECT sname NOM, city VILLE FROM s");
        solutionRepository.save(s24);
    }

    private void createQuestionsQuiz5() {
        Quiz quiz5 = quizRepository.findById(5L).orElseThrow();

        Question q32 = new Question();
        q32.setId(32L);
        q32.setQuiz(quiz5);
        q32.setOrder(1);
        q32.setBody("On veut le nom et la ville de tous les fournisseurs avec comme intitulé NOM et VILLE.");
        questionRepository.save(q32);

        Solution s25 = new Solution();
        s25.setId(25L);
        s25.setQuestion(q32);
        s25.setOrder(1);
        s25.setSql("SELECT sname NOM, city VILLE FROM s");
        solutionRepository.save(s25);
    }

    private void createQuestionsQuiz6() {
        Quiz quiz6 = quizRepository.findById(6L).orElseThrow();

        Question q33 = new Question();
        q33.setId(33L);
        q33.setQuiz(quiz6);
        q33.setOrder(1);
        q33.setBody("Affichez le nom des personnes qui ont plus de trente ans");
        questionRepository.save(q33);

        Question q34 = new Question();
        q34.setId(34L);
        q34.setQuiz(quiz6);
        q34.setOrder(2);
        q34.setBody("Affichez la date d'expédition des messages envoyés par Paul");
        questionRepository.save(q34);

        Solution s26 = new Solution();
        s26.setId(26L);
        s26.setQuestion(q33);
        s26.setOrder(1);
        s26.setSql("SELECT DISTINCT p.Nom\nFROM Personne p\nWHERE p.Age >= 30;");
        solutionRepository.save(s26);

        Solution s27 = new Solution();
        s27.setId(27L);
        s27.setQuestion(q34);
        s27.setOrder(1);
        s27.setSql("SELECT DISTINCT m.Date_Expedition \nFROM message m\n\tJOIN personne p ON p.SSN = m.Expediteur\nWHERE p.Nom = 'Paul';");
        solutionRepository.save(s27);
    }

    private void createAttempts() {
        Quiz quiz1 = quizRepository.findById(1L).orElseThrow();
        Quiz quiz4 = quizRepository.findById(4L).orElseThrow();
        Student bob = studentRepository.findById(4L).orElseThrow();
        Question q1 = questionRepository.findById(1L).orElseThrow();
        Question q30 = questionRepository.findById(30L).orElseThrow();
        Question q31 = questionRepository.findById(31L).orElseThrow();

        // Attempt 1
        Attempt attempt1 = new Attempt();
        attempt1.setId(1L);
        attempt1.setStart(OffsetDateTime.of(2023, 9, 1, 0, 0, 0, 0, OffsetDateTime.now().getOffset()));
        attempt1.setQuiz(quiz1);
        attempt1.setStudent(bob);
        attemptRepository.save(attempt1);

        Answer answer1 = new Answer();
        answer1.setId(1L);
        answer1.setQuestion(q1);
        answer1.setAttempt(attempt1);
        answer1.setSql("SELECT * FROM S");
        answer1.setCorrect(true);
        answerRepository.save(answer1);

        // Attempt 2
        Attempt attempt2 = new Attempt();
        attempt2.setId(2L);
        attempt2.setStart(OffsetDateTime.now());
        attempt2.setFinish(OffsetDateTime.now());
        attempt2.setQuiz(quiz4);
        attempt2.setStudent(bob);
        attemptRepository.save(attempt2);

        Answer answer2 = new Answer();
        answer2.setId(2L);
        answer2.setQuestion(q30);
        answer2.setAttempt(attempt2);
        answer2.setSql("SELECT * FROM S");
        answer2.setCorrect(true);
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setId(3L);
        answer3.setQuestion(q31);
        answer3.setAttempt(attempt2);
        answer3.setSql("SELECT * FROM J");
        answer3.setCorrect(false);
        answerRepository.save(answer3);
    }
}