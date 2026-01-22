package com.example.myapp013aeducationgame

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapp013aeducationgame.data.CategoryScore
import com.example.myapp013aeducationgame.data.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizActivity : AppCompatActivity() {

    private var questionsList: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private var score = 0
    private var currentCategory: String = "pavouci"

    private lateinit var tvQuestion: TextView
    private lateinit var tvScore: TextView
    private lateinit var btnOption1: Button
    private lateinit var btnOption2: Button
    private lateinit var btnOption3: Button
    private lateinit var btnOption4: Button
    private var defaultButtonColor: ColorStateList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        currentCategory = intent.getStringExtra("CATEGORY_KEY") ?: "pavouci"

        tvQuestion = findViewById(R.id.tvQuestion)
        // ZDE JSME SMAZALI findViewById pro obrázek
        tvScore = findViewById(R.id.tvScore)
        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnOption3 = findViewById(R.id.btnOption3)
        btnOption4 = findViewById(R.id.btnOption4)
        defaultButtonColor = btnOption1.backgroundTintList

        loadQuestionsFromDb()

        btnOption1.setOnClickListener { checkAnswer(0) }
        btnOption2.setOnClickListener { checkAnswer(1) }
        btnOption3.setOnClickListener { checkAnswer(2) }
        btnOption4.setOnClickListener { checkAnswer(3) }
    }

    private fun loadQuestionsFromDb() {
        val db = (application as MyApplication).database
        val dao = db.quizDao()

        lifecycleScope.launch(Dispatchers.IO) {
            questionsList = dao.getQuestionsByCategory(currentCategory)

            // --- SEEDOVÁNÍ 100 OTÁZEK ---
            if (questionsList.isEmpty()) {
                val q = mutableListOf<Question>()

                if (currentCategory == "pavouci") {
                    q.add(Question(text="Kolik nohou má pavouk?", optionA="4", optionB="6", optionC="8", optionD="10", correctOptionIndex=2, category="pavouci"))
                    q.add(Question(text="Co pavouci NEmají?", optionA="Kusadla", optionB="Tykadla", optionC="Oči", optionD="Nohy", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Jak se jmenuje strach z pavouků?", optionA="Arachnofobie", optionB="Agorafobie", optionC="Klaustrofobie", optionD="Akrofobie", correctOptionIndex=0, category="pavouci"))
                    q.add(Question(text="Který pavouk je v ČR nejjedovatější (bolestivé kousnutí)?", optionA="Křižák obecný", optionB="Zápřednice jedovatá", optionC="Pokoutník domácí", optionD="Sekáč", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Z čeho je pavučina?", optionA="Celulóza", optionB="Keratin", optionC="Tekuté hedvábí", optionD="Vlna", correctOptionIndex=2, category="pavouci"))
                    q.add(Question(text="Kolik očí má většina pavouků?", optionA="2", optionB="4", optionC="6", optionD="8", correctOptionIndex=3, category="pavouci"))
                    q.add(Question(text="Patří pavouci mezi hmyz?", optionA="Ano", optionB="Ne", optionC="Jen někteří", optionD="Záleží na velikosti", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Čím pavouci dýchají?", optionA="Žábrami", optionB="Plicními vaky", optionC="Pusou", optionD="Nosem", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Který pavouk se potápí pod vodu?", optionA="Vodouch stříbřitý", optionB="Křižák vodní", optionC="Tarantule říční", optionD="Lovčík vodní", correctOptionIndex=0, category="pavouci"))
                    q.add(Question(text="Jak loví křižák?", optionA="Skáče na kořist", optionB="Do sítě", optionC="Plive jed", optionD="Honičkou", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Jak loví skákavka?", optionA="Do sítě", optionB="Skokem na kořist", optionC="Láká na světlo", optionD="Čeká v noře", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Největší pavouk světa je?", optionA="Sklípkan největší", optionB="Černá vdova", optionC="Běžník", optionD="Tarantule obrovská", correctOptionIndex=0, category="pavouci"))
                    q.add(Question(text="K čemu slouží pavoukům chloupky?", optionA="K zahřátí", optionB="Jako smyslový orgán", optionC="K létání", optionD="Pro okrasu", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Žerou pavouci rostliny?", optionA="Ano, běžně", optionB="Ne, jsou dravci", optionC="Jen ovoce", optionD="Jen trávu", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Kolikrát se pavouk svléká?", optionA="Nikdy", optionB="Jednou", optionC="Mnohokrát za život", optionD="Každý den", correctOptionIndex=2, category="pavouci"))
                    q.add(Question(text="Co udělá sameček po páření u Černé vdovy?", optionA="Usne", optionB="Uteče", optionC="Často je sežrán", optionD="Staví dům", correctOptionIndex=2, category="pavouci"))
                    q.add(Question(text="Mají pavouci kosti?", optionA="Ano", optionB="Ne, mají vnější kostru", optionC="Jen v nohách", optionD="Jen v hlavě", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Jak se jmenuje pavouk s křížem na zádech?", optionA="Křižák obecný", optionB="Templář", optionC="Zápřednice", optionD="Pokoutník", correctOptionIndex=0, category="pavouci"))
                    q.add(Question(text="Je sekáč pavouk?", optionA="Ano", optionB="Ne, je to pavoukovec (příbuzný)", optionC="Je to hmyz", optionD="Je to korýš", correctOptionIndex=1, category="pavouci"))
                    q.add(Question(text="Dožijí se pavouci více let?", optionA="Ne, jen měsíc", optionB="Ano, sklípkani i 20 let", optionC="Max 1 rok", optionD="Max 1 týden", correctOptionIndex=1, category="pavouci"))
                }
                else if (currentCategory == "vesmir") {
                    q.add(Question(text="Nejbližší planeta ke Slunci?", optionA="Venuše", optionB="Merkur", optionC="Země", optionD="Mars", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Naše galaxie se jmenuje?", optionA="Mléčná dráha", optionB="Andromeda", optionC="Sombrero", optionD="Orion", correctOptionIndex=0, category="vesmir"))
                    q.add(Question(text="První člověk ve vesmíru?", optionA="Armstrong", optionB="Gagarin", optionC="Aldrin", optionD="Remek", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Která planeta je 'Rudá'?", optionA="Jupiter", optionB="Mars", optionC="Saturn", optionD="Venuše", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Největší planeta sluneční soustavy?", optionA="Země", optionB="Jupiter", optionC="Saturn", optionD="Neptun", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Kolik měsíců má Země?", optionA="1", optionB="2", optionC="0", optionD="4", correctOptionIndex=0, category="vesmir"))
                    q.add(Question(text="Co je Slunce?", optionA="Planeta", optionB="Hvězda", optionC="Asteroid", optionD="Měsíc", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Jak se jmenuje měsíc Země?", optionA="Titan", optionB="Europa", optionC="Měsíc", optionD="Phobos", correctOptionIndex=2, category="vesmir"))
                    q.add(Question(text="Kdo byl první na Měsíci?", optionA="Gagarin", optionB="Armstrong", optionC="Laika", optionD="Tesla", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Která planeta má prstence?", optionA="Mars", optionB="Saturn", optionC="Merkur", optionD="Venuše", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Jaká planeta je 'večernice'?", optionA="Mars", optionB="Venuše", optionC="Jupiter", optionD="Merkur", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Je Pluto planeta?", optionA="Ano", optionB="Ne, trpasličí planeta", optionC="Je to hvězda", optionD="Je to asteroid", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Co je uprostřed Mléčné dráhy?", optionA="Slunce", optionB="Černá díra", optionC="Země", optionD="Nic", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="První československý kosmonaut?", optionA="Gagarin", optionB="Remek", optionC="Pelikán", optionD="Hustoles", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Jak vznikl vesmír?", optionA="Velký třesk", optionB="Velký křach", optionC="Pomalu", optionD="Výbuchem Slunce", correctOptionIndex=0, category="vesmir"))
                    q.add(Question(text="Nejchladnější planeta?", optionA="Merkur", optionB="Neptun", optionC="Uran", optionD="Mars", correctOptionIndex=2, category="vesmir"))
                    q.add(Question(text="Která planeta je modrá?", optionA="Země", optionB="Mars", optionC="Venuše", optionD="Merkur", correctOptionIndex=0, category="vesmir"))
                    q.add(Question(text="Kolik planet má sluneční soustava?", optionA="7", optionB="8", optionC="9", optionD="10", correctOptionIndex=1, category="vesmir"))
                    q.add(Question(text="Co je to kometa?", optionA="Kus ledu a prachu", optionB="Hvězda", optionC="Planeta", optionD="Černá díra", correctOptionIndex=0, category="vesmir"))
                    q.add(Question(text="Jak dlouho letí světlo ze Slunce na Zemi?", optionA="1 sekundu", optionB="8 minut", optionC="1 rok", optionD="1 hodinu", correctOptionIndex=1, category="vesmir"))
                }
                else if (currentCategory == "dejepis") {
                    q.add(Question(text="Kdy začala 1. světová válka?", optionA="1914", optionB="1918", optionC="1939", optionD="1945", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdy vzniklo Československo?", optionA="1918", optionB="1945", optionC="1968", optionD="1993", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdo byl Karel IV.?", optionA="Prezident", optionB="Císař a král", optionC="Generál", optionD="Spisovatel", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Kdo upálili v Kostnici?", optionA="Jana Žižku", optionB="Jana Husa", optionC="Karla IV.", optionD="Václava Havla", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Kdy byla objevena Amerika?", optionA="1492", optionB="1500", optionC="1620", optionD="1212", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdo objevil Ameriku?", optionA="Vikingové", optionB="Kolumbus", optionC="Magalhães", optionD="Marco Polo", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="První prezident ČR?", optionA="Masaryk", optionB="Havel", optionC="Klaus", optionD="Zeman", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Datum Sametové revoluce?", optionA="17.11.1989", optionB="21.8.1968", optionC="28.10.1918", optionD="1.1.1993", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdo byl praotec Čech?", optionA="Bájný zakladatel", optionB="První král", optionC="Svatý", optionD="Bojovník", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdy skončila 2. světová válka?", optionA="1918", optionB="1939", optionC="1945", optionD="1968", correctOptionIndex=2, category="dejepis"))
                    q.add(Question(text="Starověký Egypt je známý díky?", optionA="Pyramidám", optionB="Hradům", optionC="Mrakodrapům", optionD="Rytířům", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdo vládl v Římě?", optionA="Faraon", optionB="Císař", optionC="Car", optionD="Prezident", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Kde žili Aztékové?", optionA="Evropa", optionB="Mexiko", optionC="Čína", optionD="Austrálie", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Zlatá bula sicilská (rok)?", optionA="1212", optionB="1348", optionC="1526", optionD="1620", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Bitva na Bílé hoře?", optionA="1620", optionB="1420", optionC="1526", optionD="1212", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdo byl 'Učitel národů'?", optionA="Hus", optionB="Komenský", optionC="Palacký", optionD="Masaryk", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Národní obrození - jazyk?", optionA="Němčina", optionB="Čeština", optionC="Angličtina", optionD="Polština", correctOptionIndex=1, category="dejepis"))
                    q.add(Question(text="Kdo postavil Karlův most?", optionA="Karel IV.", optionB="Václav I.", optionC="Rudolf II.", optionD="Boleslav", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Patron české země?", optionA="Sv. Václav", optionB="Sv. Patrik", optionC="Sv. Jiří", optionD="Sv. Mikuláš", correctOptionIndex=0, category="dejepis"))
                    q.add(Question(text="Kdy vznikla samostatná Česká republika?", optionA="1.1.1993", optionB="1.1.1918", optionC="28.10.1918", optionD="17.11.1989", correctOptionIndex=0, category="dejepis"))
                }
                else if (currentCategory == "zemepis") {
                    q.add(Question(text="Hlavní město ČR?", optionA="Brno", optionB="Praha", optionC="Ostrava", optionD="Plzeň", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Hlavní město Slovenska?", optionA="Košice", optionB="Bratislava", optionC="Nitra", optionD="Žilina", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Nejvyšší hora světa?", optionA="K2", optionB="Mount Everest", optionC="Mont Blanc", optionD="Sněžka", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Nejvyšší hora ČR?", optionA="Praděd", optionB="Sněžka", optionC="Říp", optionD="Lysá hora", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Kde leží Eiffelova věž?", optionA="Londýn", optionB="Paříž", optionC="Berlín", optionD="Řím", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Nejdelší řeka v ČR?", optionA="Labe", optionB="Vltava", optionC="Morava", optionD="Odra", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Který světadíl je největší?", optionA="Afrika", optionB="Asie", optionC="Evropa", optionD="Amerika", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Kde žijí klokani?", optionA="Rakousko", optionB="Austrálie", optionC="Afrika", optionD="Amerika", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Hlavní město USA?", optionA="New York", optionB="Washington D.C.", optionC="Los Angeles", optionD="Chicago", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Které moře je u Chorvatska?", optionA="Jaderské", optionB="Baltské", optionC="Severní", optionD="Černé", correctOptionIndex=0, category="zemepis"))
                    q.add(Question(text="Sousedí ČR s Polskem?", optionA="Ano", optionB="Ne", optionC="Jen přes moře", optionD="Nevím", correctOptionIndex=0, category="zemepis"))
                    q.add(Question(text="Kolik krajů má ČR?", optionA="10", optionB="12", optionC="14", optionD="16", correctOptionIndex=2, category="zemepis"))
                    q.add(Question(text="Hlavní město Německa?", optionA="Mnichov", optionB="Berlín", optionC="Frankfurt", optionD="Hamburk", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Kde jsou pyramidy?", optionA="Řecko", optionB="Egypt", optionC="Itálie", optionD="Turecko", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Co je to Sahara?", optionA="Řeka", optionB="Poušť", optionC="Hora", optionD="Město", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Kde je Socha svobody?", optionA="Paříž", optionB="New York", optionC="Londýn", optionD="Sydney", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Jaký oceán je mezi Evropou a Amerikou?", optionA="Tichý", optionB="Atlantský", optionC="Indický", optionD="Severní", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Hlavní město Ruska?", optionA="Petrohrad", optionB="Moskva", optionC="Kyjev", optionD="Minsk", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Kde se platí Eurem?", optionA="USA", optionB="Většina EU", optionC="Anglie", optionD="ČR", correctOptionIndex=1, category="zemepis"))
                    q.add(Question(text="Nejlidnatější stát světa?", optionA="Indie", optionB="USA", optionC="Rusko", optionD="Brazílie", correctOptionIndex=0, category="zemepis"))
                }
                else if (currentCategory == "telo") {
                    q.add(Question(text="Co pumpuje krev?", optionA="Játra", optionB="Srdce", optionC="Ledviny", optionD="Plíce", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kde sídlí myšlení?", optionA="Srdce", optionB="Mozek", optionC="Žaludek", optionD="Páteř", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kolik má dospělý zubů?", optionA="20", optionB="32", optionC="50", optionD="28", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Co chrání mozek?", optionA="Lebka", optionB="Žebra", optionC="Pánev", optionD="Kůže", correctOptionIndex=0, category="telo"))
                    q.add(Question(text="Největší orgán těla?", optionA="Játra", optionB="Kůže", optionC="Střeva", optionD="Mozek", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Co dýcháme?", optionA="Dusík", optionB="Kyslík", optionC="Vodík", optionD="Helium", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kolik kostí má dospělý (cca)?", optionA="100", optionB="206", optionC="300", optionD="500", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kde se tráví jídlo?", optionA="V plicích", optionB="V žaludku a střevech", optionC="V ledvinách", optionD="V srdci", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Co je červené v krvi?", optionA="Krvinky", optionB="Voda", optionC="Cukr", optionD="Tuk", correctOptionIndex=0, category="telo"))
                    q.add(Question(text="Kolik máme ledvin?", optionA="1", optionB="2", optionC="3", optionD="4", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Nejdelší kost v těle?", optionA="Pažní", optionB="Stehenní", optionC="Holenní", optionD="Páteř", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Co spojuje svaly s kostmi?", optionA="Šlachy", optionB="Kůže", optionC="Cévy", optionD="Nervy", correctOptionIndex=0, category="telo"))
                    q.add(Question(text="Co filtruje krev?", optionA="Srdce", optionB="Ledviny", optionC="Žaludek", optionD="Mozek", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Jak se jmenuje barevná část oka?", optionA="Bělima", optionB="Duhovka", optionC="Zornice", optionD="Sítnice", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Co nám umožňuje slyšet?", optionA="Oči", optionB="Uši", optionC="Nos", optionD="Ústa", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kde je biceps?", optionA="Na noze", optionB="Na paži", optionC="Na břiše", optionD="Na zádech", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Normální teplota těla?", optionA="30°C", optionB="37°C", optionC="40°C", optionD="35°C", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Kolik litrů krve má dospělý?", optionA="1 litr", optionB="5 litrů", optionC="10 litrů", optionD="20 litrů", correctOptionIndex=1, category="telo"))
                    q.add(Question(text="Který vitamín je ze slunce?", optionA="A", optionB="C", optionC="D", optionD="E", correctOptionIndex=2, category="telo"))
                    q.add(Question(text="Co je DNA?", optionA="Nositel dědičnosti", optionB="Druh krve", optionC="Kost", optionD="Nemoc", correctOptionIndex=0, category="telo"))
                }

                if (q.isNotEmpty()) {
                    dao.insertQuestions(q)
                    questionsList = dao.getQuestionsByCategory(currentCategory)
                }
            }

            questionsList = questionsList.shuffled()

            withContext(Dispatchers.Main) {
                if (questionsList.isNotEmpty()) {
                    showQuestion()
                } else {
                    tvQuestion.text = "Načítám otázky..."
                }
            }
        }
    }

    private fun showQuestion() {
        resetButtonColors()

        if (currentQuestionIndex >= questionsList.size) {
            gameOver()
            return
        }

        val q = questionsList[currentQuestionIndex]

        tvQuestion.text = q.text
        btnOption1.text = q.optionA
        btnOption2.text = q.optionB
        btnOption3.text = q.optionC
        btnOption4.text = q.optionD
    }

    private fun checkAnswer(selectedOptionIndex: Int) {
        disableButtons()
        val q = questionsList[currentQuestionIndex]
        val buttons = listOf(btnOption1, btnOption2, btnOption3, btnOption4)
        val selectedButton = buttons[selectedOptionIndex]
        val correctButton = buttons[q.correctOptionIndex]

        if (selectedOptionIndex == q.correctOptionIndex) {
            score++
            tvScore.text = "Skóre: $score"
            selectedButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        } else {
            selectedButton.backgroundTintList = ColorStateList.valueOf(Color.RED)
            correctButton.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
        }

        lifecycleScope.launch {
            delay(1500)
            currentQuestionIndex++
            showQuestion()
        }
    }

    private fun resetButtonColors() {
        val buttons = listOf(btnOption1, btnOption2, btnOption3, btnOption4)
        for (btn in buttons) {
            btn.backgroundTintList = defaultButtonColor
            btn.isEnabled = true
        }
    }

    private fun disableButtons() {
        btnOption1.isEnabled = false
        btnOption2.isEnabled = false
        btnOption3.isEnabled = false
        btnOption4.isEnabled = false
    }

    private fun gameOver() {
        saveUserScore()
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("SCORE_KEY", score)
        intent.putExtra("TOTAL_KEY", questionsList.size)
        startActivity(intent)
        finish()
    }

    private fun saveUserScore() {
        val db = (application as MyApplication).database.quizDao()
        val finalScore = score

        lifecycleScope.launch(Dispatchers.IO) {
            val oldRecord = db.getCategoryScore(currentCategory)?.bestScore ?: 0

            if (finalScore > oldRecord) {
                db.insertOrUpdateScore(CategoryScore(currentCategory, finalScore))
            }
        }
    }
}