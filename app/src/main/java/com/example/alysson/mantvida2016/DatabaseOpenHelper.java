package com.example.alysson.mantvida2016;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by Alysson on 17/11/2015.
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {

        //Por ver das dúvidas, vou deixar a versão 5.
        private static final String BD_NAME = "BDMant";
        private static final int BD_VERSION = 7;

        //BD_VERSION = 7 (version 7 is in google play, the next version 8)


        public DatabaseOpenHelper(Context ctx) {
                super(ctx, BD_NAME, null, BD_VERSION);
        }

        //Vai dar certo para glória do Senhor Jesus.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                switch (newVersion) {

                        //Update 6 - To Correct the readings of 3 first months.
                        case 7:
                                update7Table1(db);
                }

        }

    /**
     *
     * @param db db of application
     * this method update the wrong data of table "tblleitura_1tri"
     */
        private void update7Table1(SQLiteDatabase db) {


                int[] arrayId = new int[]{6, 7, 8, 9, 12, 32, 33, 34, 51, 61, 83, 116, 147, 152};
                int[] arrayMes = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3};
                int[] arrayDia = new int[]{3, 3, 4, 4, 6, 16, 16, 16, 28, 1, 10, 4, 19, 22};
                String[] arrayTitulo = new String[]{"Jr 1.1-19", "Jr 2.1-3", "Mt 22.23-33", "Mt 22.41-46",
                        "Lc 20.27-44", "Ex 7.1-25", "Ex 8.1-32", "Ex 9.1-35", "2 Co 8.1-15",
                        "Mt 19.16-30", "Mc 7.1-23", "2 Co 3.11-18", "Lv 5.1-19", "Mc 12.28-30" };

                String[] arrayTexto = new String[]{"1 Palavras de Jeremias, filho de Hilquias, um dos sacerdotes que estavam em Anatote, na terra de Benjamim;%\n" +
                        "2 Ao qual veio a palavra do SENHOR, nos dias de Josias, filho de Amom, rei de Judá, no décimo terceiro ano do seu reinado.%\n" +
                        "3 E lhe veio também nos dias de Jeoiaquim, filho de Josias, rei de Judá, até ao fim do ano undécimo de Zedequias, filho de Josias, rei de Judá, até que Jerusalém foi levada em cativeiro no quinto mês.%\n" +
                        "4 Assim veio a mim a palavra do SENHOR, dizendo:%\n" +
                        "5 Antes que te formasse no ventre te conheci, e antes que saísses da madre, te santifiquei; às nações te dei por profeta.%\n" +
                        "6 Então disse eu: Ah, Senhor DEUS! Eis que não sei falar; porque ainda sou um menino.%\n" +
                        "7 Mas o SENHOR me disse: Não digas: Eu sou um menino; porque a todos a quem eu te enviar, irás; e tudo quanto te mandar, falarás.%\n" +
                        "8 Não temas diante deles; porque estou contigo para te livrar, diz o SENHOR.%\n" +
                        "9 E estendeu o SENHOR a sua mão, e tocou-me na boca; e disse-me o SENHOR: Eis que ponho as minhas palavras na tua boca;%\n" +
                        "10 Olha, ponho-te neste dia sobre as nações, e sobre os reinos, para arrancares, e para derrubares, e para destruíres, e para arruinares; e também para edificares e para plantares.%\n" +
                        "11 Ainda veio a mim a palavra do SENHOR, dizendo: Que é que vês, Jeremias? E eu disse: Vejo uma vara de amendoeira.%\n" +
                        "12 E disse-me o SENHOR: Viste bem; porque eu velo sobre a minha palavra para cumpri-la.%\n" +
                        "13 E veio a mim a palavra do SENHOR segunda vez, dizendo: Que é que vês? E eu disse: Vejo uma panela a ferver, cuja face está para o lado do norte.%\n" +
                        "14 E disse-me o SENHOR: Do norte se descobrirá o mal sobre todos os habitantes da terra.%\n" +
                        "15 Porque eis que eu convoco todas as famílias dos reinos do norte, diz o SENHOR; e virão, e cada um porá o seu trono à entrada das portas de Jerusalém, e contra todos os seus muros em redor, e contra todas as cidades de Judá.%\n" +
                        "16 E eu pronunciarei contra eles os meus juízos, por causa de toda a sua malícia; pois me deixaram, e queimaram incenso a deuses estranhos, e se encurvaram diante das obras das suas mãos.%\n" +
                        "17 Tu, pois, cinge os teus lombos, e levanta-te, e dize-lhes tudo quanto eu te mandar; não te espantes diante deles, para que eu não te envergonhe diante deles.%\n" +
                        "18 Porque, eis que hoje te ponho por cidade forte, e por coluna de ferro, e por muros de bronze, contra toda a terra, contra os reis de Judá, contra os seus príncipes, contra os seus sacerdotes, e contra o povo da terra.%\n" +
                        "19 E pelejarão contra ti, mas não prevalecerão contra ti; porque eu sou contigo, diz o SENHOR, para te livrar.",

                        "1 E veio a mim a palavra do SENHOR, dizendo:%\n" +
                                "2 Vai, e clama aos ouvidos de Jerusalém, dizendo: Assim diz o SENHOR: Lembro-me de ti, da piedade da tua mocidade, e do amor do teu noivado, quando me seguias no deserto, numa terra que não se semeava.%\n" +
                                "3 Então Israel era santidade para o SENHOR, e as primícias da sua novidade; todos os que o devoravam eram tidos por culpados; o mal vinha sobre eles, diz o SENHOR.",

                        "23 No mesmo dia vieram alguns saduceus, que dizem não haver ressurreição, e o interrogaram, dizendo:%\n" +
                                "24 Mestre, Moisés disse: Se morrer alguém, não tendo filhos, seu irmão casará com a mulher dele, e suscitará descendência a seu irmão.%\n" +
                                "25 Ora, havia entre nós sete irmãos: o primeiro, tendo casado, morreu: e, não tendo descendência, deixou sua mulher a seu irmão;%\n" +
                                "26 da mesma sorte também o segundo, o terceiro, até o sétimo.%\n" +
                                "27 depois de todos, morreu também a mulher.%\n" +
                                "28 Portanto, na ressurreição, de qual dos sete será ela esposa, pois todos a tiveram?%\n" +
                                "29 Jesus, porém, lhes respondeu: Errais, não compreendendo as Escrituras nem o poder de Deus;%\n" +
                                "30 pois na ressurreição nem se casam nem se dão em casamento; mas serão como os anjos no céu.%\n" +
                                "31 E, quanto à ressurreição dos mortos, não lestes o que foi dito por Deus:%\n" +
                                "32 Eu sou o Deus de Abraão, o Deus de Isaque, e o Deus de Jacó? Ora, ele não é Deus de mortos, mas de vivos.%\n" +
                                "33 E as multidões, ouvindo isso, se maravilhavam da sua doutrina.",

                        "41 Ora, enquanto os fariseus estavam reunidos, interrogou-os Jesus, dizendo:%\n" +
                                "42 Que pensais vós do Cristo? De quem é filho? Responderam-lhe: De Davi.%\n" +
                                "43 Replicou-lhes ele: Como é então que Davi, no Espírito, lhe chama Senhor, dizendo:%\n" +
                                "44 Disse o Senhor ao meu Senhor: Assenta-te à minha direita, até que eu ponha os teus inimigos de baixo dos teus pés?%\n" +
                                "45 Se Davi, pois, lhe chama Senhor, como é ele seu filho?%\n" +
                                "46 E ninguém podia responder-lhe palavra; nem desde aquele dia jamais ousou alguém interrogá-lo.\n",

                        "27 Chegaram então alguns dos saduceus, que dizem não haver ressurreição, e perguntaram-lhe:%\n" +
                                "28 Mestre, Moisés nos deixou escrito que se morrer alguém, tendo mulher mas não tendo filhos, o irmão dele case com a viúva, e suscite descendência ao irmão.%\n" +
                                "29 Havia, pois, sete irmãos. O primeiro casou-se e morreu sem filhos;%\n" +
                                "30 então o segundo, e depois o terceiro, casaram com a viúva;%\n" +
                                "31 e assim todos os sete, e morreram, sem deixar filhos.%\n" +
                                "32 Depois morreu também a mulher.%\n" +
                                "33 Portanto, na ressurreição, de qual deles será ela esposa, pois os sete por esposa a tiveram?%\n" +
                                "34 Respondeu-lhes Jesus: Os filhos deste mundo casaram-se e dão-se em casamento;%\n" +
                                "35 mas os que são julgados dignos de alcançar o mundo vindouro, e a ressurreição dentre os mortos, nem se casam nem se dão em casamento;%\n" +
                                "36 porque já não podem mais morrer; pois são iguais aos anjos, e são filhos de Deus, sendo filhos da ressurreição.%\n" +
                                "37 Mas que os mortos hão de ressurgir, o próprio Moisés o mostrou, na passagem a respeito da sarça, quando chama ao Senhor; Deus de Abraão, e Deus de Isaque, e Deus de Jacó.%\n" +
                                "38 Ora, ele não é Deus de mortos, mas de vivos; porque para ele todos vivem.%\n" +
                                "39 Responderam alguns dos escribas: Mestre, disseste bem.%\n" +
                                "40 Não ousavam, pois, perguntar-lhe mais coisa alguma.%\n" +
                                "41 Jesus, porém, lhes perguntou: Como dizem que o Cristo é filho de Davi?%\n" +
                                "42 Pois o próprio Davi diz no livro dos Salmos: Disse o Senhor ao meu Senhor: Assenta-te à minha direita,%\n" +
                                "43 até que eu ponha os teus inimigos por escabelo dos teus pés.%\n" +
                                "44 Logo Davi lhe chama Senhor como, pois, é ele seu filho?",

                        "1 Então disse o Senhor a Moisés: Eis que te tenho posto como Deus a Faraó, e Arão, teu irmão, será o teu profeta.%\n" +
                                "2 Tu falarás tudo o que eu te ordenar; e Arão, teu irmão, falará a Faraó, para que deixe ir da sua terra os filhos de Israel.%\n" +
                                "3 Eu, porém, endurecerei o coração de Faraó e multiplicarei na terra do Egito os meus sinais e as minhas maravilhas.%\n" +
                                "4 Faraó não vos ouvirá; e eu porei a mão sobre o Egito e farei sair as minhas hostes, o meu povo, os filhos de Israel, da terra do Egito, com grandes manifestações de julgamento.%\n" +
                                "5 Saberão os egípcios que eu sou o SENHOR, quando estender eu a mão sobre o Egito e tirar do meio deles os filhos de Israel.%\n" +
                                "6 Assim fez Moisés e Arão; como o SENHOR lhes ordenara, assim fizeram.%\n" +
                                "7 Era Moisés de oitenta anos, e Arão, de oitenta e três, quando falaram a Faraó.%\n" +
                                "8 Falou o SENHOR a Moisés e a Arão:%\n" +
                                "9 Quando Faraó vos disser: Fazei milagres que vos acreditem, dirás a Arão: Toma o teu bordão e lança-o diante de Faraó; e o bordão se tornará em serpente.%\n" +
                                "10 Então, Moisés e Arão se chegaram a Faraó e fizeram como o SENHOR lhes ordenara; lançou Arão o seu bordão diante de Faraó e diante dos seus oficiais, e ele se tornou em% serpente.%\n" +
                                "11 Faraó, porém, mandou vir os sábios e encantadores; e eles, os sábios do Egito, fizeram também o mesmo com as suas ciências ocultas.%\n" +
                                "12 Pois lançaram eles cada um o seu bordão, e eles se tornaram em serpentes; mas o bordão de Arão devorou os bordões deles.%\n" +
                                "13 Todavia, o coração de Faraó se endureceu, e não os ouviu, como o SENHOR tinha dito.%\n" +
                                "14 Disse o SENHOR a Moisés: O coração de Faraó está obstinado; recusa deixar ir o povo.%\n" +
                                "15 Vai ter com Faraó pela manhã; ele sairá às águas; estarás à espera dele na beira do rio, tomarás na mão o bordão que se tornou em serpente%\n" +
                                "16 e lhe dirás: O SENHOR, o Deus dos hebreus, me enviou a ti para te dizer: Deixa ir o meu povo, para que me sirva no deserto; e, até agora, não tens ouvido.%\n" +
                                "17 Assim diz o SENHOR: Nisto saberás que eu sou o SENHOR: com este bordão que tenho na mão ferirei as águas do rio, e se tornarão em sangue.%\n" +
                                "18 Os peixes que estão no rio morrerão, o rio cheirará mal, e os egípcios terão nojo de beber água do rio.%\n" +
                                "19 Disse mais o SENHOR a Moisés: Dize a Arão: toma o teu bordão e estende a mão sobre as águas do Egito, sobre os seus rios, sobre os seus canais, sobre as suas lagoas e sobre todos os seus reservatórios, para que se tornem em sangue; haja sangue em toda a terra do Egito, tanto nos vasos de madeira como nos de pedra.%\n" +
                                "20 Fizeram Moisés e Arão como o SENHOR lhes havia ordenado: Arão, levantando o bordão, feriu as águas que estavam no rio, à vista de Faraó e seus oficiais; e toda a água do rio se tornou em sangue.%\n" +
                                "21 De sorte que os peixes que estavam no rio morreram, o rio cheirou mal, e os egípcios não podiam beber a água do rio; e houve sangue por toda a terra do Egito.%\n" +
                                "22 Porém os magos do Egito fizeram também o mesmo com as suas ciências ocultas; de maneira que o coração de Faraó se endureceu, e não os ouviu, como o SENHOR tinha dito.%\n" +
                                "23 Virou-se Faraó e foi para casa; nem ainda isso considerou o seu coração.%\n" +
                                "24 Todos os egípcios cavaram junto ao rio para encontrar água que beber, pois das águas do rio não podiam beber.%\n" +
                                "25 Assim se passaram sete dias, depois que o SENHOR feriu o rio.",

                        "1 Então disse o Senhor a Moisés: Vai a Faraó, e dize-lhe: Assim diz o Senhor: Deixa ir o meu povo, para que me sirva.%\n" +
                                "2 Se recusares deixá-lo ir, eis que castigarei com rãs todos os teus territórios.%\n" +
                                "3 O rio produzirá rãs em abundância, que subirão e entrarão em tua casa, e no teu quarto de dormir, e sobre o teu leito, e nas casas dos teus oficiais, e sobre o teu povo, e nos teus fornos, e nas tuas amassadeiras.%\n" +
                                "4 As rãs virão sobre ti, sobre o teu povo e sobre todos os teus oficiais.%\n" +
                                "5 Disse mais o SENHOR a Moisés: Dize a Arão: Estende a mão com o teu bordão sobre os rios, sobre os canais e sobre as lagoas e faze subir rãs sobre a terra do Egito.%\n" +
                                "6 Arão estendeu a mão sobre as águas do Egito, e subiram rãs e cobriram a terra do Egito.%\n" +
                                "7 Então, os magos fizeram o mesmo com suas ciências ocultas e fizeram aparecer rãs sobre a terra do Egito.%\n" +
                                "8 Chamou Faraó a Moisés e a Arão e lhes disse: Rogai ao SENHOR que tire as rãs de mim e do meu povo; então, deixarei ir o povo, para que ofereça sacrifícios ao SENHOR.%\n" +
                                "9 Falou Moisés a Faraó: Digna-te dizer-me quando é que hei de rogar por ti, pelos teus oficiais e pelo teu povo, para que as rãs sejam retiradas de ti e das tuas casas e fiquem somente no rio.%\n" +
                                "10 Ele respondeu: Amanhã. Moisés disse: Seja conforme a tua palavra, para que saibas que ninguém há como o SENHOR, nosso Deus.%\n" +
                                "11 Retirar-se-ão as rãs de ti, e das tuas casas, e dos teus oficiais, e do teu povo; ficarão somente no rio.%\n" +
                                "12 Então, saíram Moisés e Arão da presença de Faraó; e Moisés clamou ao SENHOR por causa das rãs, conforme combinara com Faraó.%\n" +
                                "13 E o SENHOR fez conforme a palavra de Moisés; morreram as rãs nas casas, nos pátios e nos campos.%\n" +
                                "14 Ajuntaram-nas em montões e montões, e a terra cheirou mal.%\n" +
                                "15 Vendo, porém, Faraó que havia alívio, continuou de coração endurecido e não os ouviu, como o SENHOR tinha dito.%\n" +
                                "16 Disse o SENHOR a Moisés: Dize a Arão: Estende o teu bordão e fere o pó da terra, para que se torne em piolhos por toda a terra do Egito.%\n" +
                                "17 Fizeram assim; Arão estendeu a mão com seu bordão e feriu o pó da terra, e houve muitos piolhos nos homens e no gado; todo o pó da terra se tornou em piolhos por toda a terra do Egito.%\n" +
                                "18 E fizeram os magos o mesmo com suas ciências ocultas para produzirem piolhos, porém não o puderam; e havia piolhos nos homens e no gado.%\n" +
                                "19 Então, disseram os magos a Faraó: Isto é o dedo de Deus. Porém o coração de Faraó se endureceu, e não os ouviu, como o SENHOR tinha dito.%\n" +
                                "20 Disse o SENHOR a Moisés: Levanta-te pela manhã cedo e apresenta-te a Faraó; eis que ele sairá às águas; e dize-lhe: Assim diz o SENHOR: Deixa ir o meu povo, para que me sirva.\n" +
                                "21 Do contrário, se tu não deixares ir o meu povo, eis que eu enviarei enxames de moscas sobre ti, e sobre os teus oficiais, e sobre o teu povo, e nas tuas casas; e as casas dos egípcios se encherão destes enxames, e também a terra em que eles estiverem.%\n" +
                                "22 Naquele dia, separarei a terra de Gósen, em que habita o meu povo, para que nela não haja enxames de moscas, e saibas que eu sou o SENHOR no meio desta terra.%\n" +
                                "23 Farei distinção entre o meu povo e o teu povo; amanhã se dará este sinal.%\n" +
                                "24 Assim fez o SENHOR; e vieram grandes enxames de moscas à casa de Faraó, e às casas dos seus oficiais, e sobre toda a terra do Egito; e a terra ficou arruinada com estes enxames.\n" +
                                "25 Chamou Faraó a Moisés e a Arão e disse: Ide, oferecei sacrifícios ao vosso Deus nesta terra.%\n" +
                                "26 Respondeu Moisés: Não convém que façamos assim porque ofereceríamos ao SENHOR, nosso Deus, sacrifícios abomináveis aos egípcios; eis que, se oferecermos tais sacrifícios perante os seus olhos, não nos apedrejarão eles?%\n" +
                                "27 Temos de ir caminho de três dias ao deserto e ofereceremos sacrifícios ao SENHOR, nosso Deus, como ele nos disser.%\n" +
                                "28 Então, disse Faraó: Deixar-vos-ei ir, para que ofereçais sacrifícios ao SENHOR, vosso Deus, no deserto; somente que, saindo, não vades muito longe; orai também por mim.%\n" +
                                "29 Respondeu-lhe Moisés: Eis que saio da tua presença e orarei ao SENHOR; amanhã, estes enxames de moscas se retirarão de Faraó, dos seus oficiais e do seu povo; somente que Faraó não mais me engane, não deixando ir o povo para que ofereça sacrifícios ao SENHOR.%\n" +
                                "30 Então, saiu Moisés da presença de Faraó e orou ao SENHOR.%\n" +
                                "31 E fez o SENHOR conforme a palavra de Moisés, e os enxames de moscas se retiraram de Faraó, dos seus oficiais e do seu povo; não ficou uma só mosca.%\n" +
                                "32 Mas ainda esta vez endureceu Faraó o coração e não deixou ir o povo.",


                        "1 Depois o Senhor disse a Moisés: Vai a Faraó e dize-lhe: Assim diz o Senhor, o Deus dos hebreus: Deixa ir o meu povo, para que me sirva.%\n" +
                                "2 Porque, se recusares deixá-los ir e ainda por força os detiveres,%\n" +
                                "3 eis que a mão do SENHOR será sobre o teu rebanho, que está no campo, sobre os cavalos, sobre os jumentos, sobre os camelos, sobre o gado e sobre as ovelhas, com pestilência gravíssima.%\n" +
                                "4 E o SENHOR fará distinção entre os rebanhos de Israel e o rebanho do Egito, para que nada morra de tudo o que pertence aos filhos de Israel.%\n" +
                                "5 O SENHOR designou certo tempo, dizendo: Amanhã, fará o SENHOR isto na terra.%\n" +
                                "6 E o SENHOR o fez no dia seguinte, e todo o rebanho dos egípcios morreu; porém, do rebanho dos israelitas, não morreu nem um.%\n" +
                                "7 Faraó mandou ver, e eis que do rebanho de Israel não morrera nem um sequer; porém o coração de Faraó se endureceu, e não deixou ir o povo.%\n" +
                                "8 Então, disse o SENHOR a Moisés e a Arão: Apanhai mãos cheias de cinza de forno, e Moisés atire-a para o céu diante de Faraó.%\n" +
                                "9 Ela se tornará em pó miúdo sobre toda a terra do Egito e se tornará em tumores que se arrebentem em úlceras nos homens e nos animais, por toda a terra do Egito.%\n" +
                                "10 Eles tomaram cinza de forno e se apresentaram a Faraó; Moisés atirou-a para o céu, e ela se tornou em tumores que se arrebentavam em úlceras nos homens e nos animais,%\n" +
                                "11 de maneira que os magos não podiam permanecer diante de Moisés, por causa dos tumores; porque havia tumores nos magos e em todos os egípcios.%\n" +
                                "12 Porém o SENHOR endureceu o coração de Faraó, e este não os ouviu, como o SENHOR tinha dito a Moisés.%\n" +
                                "13 Disse o SENHOR a Moisés: Levanta-te pela manhã cedo, apresenta-te a Faraó e dize-lhe: Assim diz o SENHOR, o Deus dos hebreus: Deixa ir o meu povo, para que me sirva.%\n" +
                                "14 Pois esta vez enviarei todas as minhas pragas sobre o teu coração, e sobre os teus oficiais, e sobre o teu povo, para que saibas que não há quem me seja semelhante em toda a terra.%\n" +
                                "15 Pois já eu poderia ter estendido a mão para te ferir a ti e o teu povo com pestilência, e terias sido cortado da terra;%\n" +
                                "16 mas, deveras, para isso te hei mantido, a fim de mostrar-te o meu poder, e para que seja o meu nome anunciado em toda a terra.%\n" +
                                "17 Ainda te levantas contra o meu povo, para não deixá-lo ir?%\n" +
                                "18 Eis que amanhã, por este tempo, farei cair mui grave chuva de pedras, como nunca houve no Egito, desde o dia em que foi fundado até hoje.%\n" +
                                "19 Agora, pois, manda recolher o teu gado e tudo o que tens no campo; todo homem e animal que se acharem no campo e não se recolherem a casa, em caindo sobre eles a chuva de pedras, morrerão.%\n" +
                                "20 Quem dos oficiais de Faraó temia a palavra do SENHOR fez fugir os seus servos e o seu gado para as casas;%\n" +
                                "21 aquele, porém, que não se importava com a palavra do SENHOR deixou ficar no campo os seus servos e o seu gado.%\n" +
                                "22 Então, disse o SENHOR a Moisés: Estende a mão para o céu, e cairá chuva de pedras em toda a terra do Egito, sobre homens, sobre animais e sobre toda planta do campo na terra do Egito.%\n" +
                                "23 E Moisés estendeu o seu bordão para o céu; o SENHOR deu trovões e chuva de pedras, e fogo desceu sobre a terra; e fez o SENHOR cair chuva de pedras sobre a terra do Egito.%\n" +
                                "24 De maneira que havia chuva de pedras e fogo misturado com a chuva de pedras tão grave, qual nunca houve em toda a terra do Egito, desde que veio a ser uma nação.%\n" +
                                "25 Por toda a terra do Egito a chuva de pedras feriu tudo quanto havia no campo, tanto homens como animais; feriu também a chuva de pedras toda planta do campo e quebrou todas as árvores do campo.%\n" +
                                "26 Somente na terra de Gósen, onde estavam os filhos de Israel, não havia chuva de pedras.%\n" +
                                "27 Então, Faraó mandou chamar a Moisés e a Arão e lhes disse: Esta vez pequei; o SENHOR é justo, porém eu e o meu povo somos ímpios.%\n" +
                                "28 Orai ao SENHOR; pois já bastam estes grandes trovões e a chuva de pedras. Eu vos deixarei ir, e não ficareis mais aqui%.\n" +
                                "29 Respondeu-lhe Moisés: Em saindo eu da cidade, estenderei as mãos ao SENHOR; os trovões cessarão, e já não haverá chuva de pedras; para que saibas que a terra é do SENHOR.\n" +
                                "30 Quanto a ti, porém, e aos teus oficiais, eu sei que ainda não temeis ao SENHOR Deus.\n" +
                                "31 (O linho e a cevada foram feridos, pois a cevada já estava na espiga, e o linho, em flor.%\n" +
                                "32 Porém o trigo e o centeio não sofreram dano, porque ainda não haviam nascido.)%\n" +
                                "33 Saiu, pois, Moisés da presença de Faraó e da cidade e estendeu as mãos ao SENHOR; cessaram os trovões e a chuva de pedras, e não caiu mais chuva sobre a terra.%\n" +
                                "34 Tendo visto Faraó que cessaram as chuvas, as pedras e os trovões, tornou a pecar e endureceu o coração, ele e os seus oficiais.%\n" +
                                "35 E assim Faraó, de coração endurecido, não deixou ir os filhos de Israel, como o SENHOR tinha dito a Moisés.",


                        "1 Também, irmãos, vos fazemos conhecer a graça de Deus concedida às igrejas da Macedônia;%\n" +
                                "2 porque, no meio de muita prova de tribulação, manifestaram abundância de alegria, e a profunda pobreza deles superabundou em grande riqueza da sua generosidade.%\n" +
                                "3 Porque eles, testemunho eu, na medida de suas posses e mesmo acima delas, se mostraram voluntários,%\n" +
                                "4 pedindo-nos, com muitos rogos, a graça de participarem da assistência aos santos.%\n" +
                                "5 E não somente fizeram como nós esperávamos, mas também deram-se a si mesmos primeiro ao Senhor, depois a nós, pela vontade de Deus;%\n" +
                                "6 o que nos levou a recomendar a Tito que, como começou, assim também complete esta graça entre vós.%\n" +
                                "7 Como, porém, em tudo, manifestais superabundância, tanto na fé e na palavra como no saber, e em todo cuidado, e em nosso amor para convosco, assim também abundeis nesta graça.%\n" +
                                "8 Não vos falo na forma de mandamento, mas para provar, pela diligência de outros, a sinceridade do vosso amor;%\n" +
                                "9 pois conheceis a graça de nosso Senhor Jesus Cristo, que, sendo rico, se fez pobre por amor de vós, para que, pela sua pobreza, vos tornásseis ricos.%\n" +
                                "10 E nisto dou minha opinião; pois a vós outros, que, desde o ano passado, principiastes não só a prática, mas também o querer, convém isto.%\n" +
                                "11 Completai, agora, a obra começada, para que, assim como revelastes prontidão no querer, assim a leveis a termo, segundo as vossas posses.%\n" +
                                "12 Porque, se há boa vontade, será aceita conforme o que o homem tem e não segundo o que ele não tem.%\n" +
                                "13 Porque não é para que os outros tenham alívio, e vós, sobrecarga; mas para que haja igualdade,%\n" +
                                "14 suprindo a vossa abundância, no presente, a falta daqueles, de modo que a abundância daqueles venha a suprir a vossa falta, e, assim, haja igualdade,%\n" +
                                "15 como está escrito: O que muito colheu não teve demais; e o que pouco, não teve falta.",


                        "16 E eis que se aproximou dele um jovem, e lhe disse: Mestre, que bem farei para conseguir a vida eterna?%\n" +
                                "17 Respondeu-lhe ele: Por que me perguntas sobre o que é bom? Um só é bom; mas se é que queres entrar na vida, guarda os mandamentos.%\n" +
                                "18 Perguntou-lhe ele: Quais? Respondeu Jesus: Não matarás; não adulterarás; não furtarás; não dirás falso testemunho;%\n" +
                                "19 honra a teu pai e a tua mãe; e amarás o teu próximo como a ti mesmo.%\n" +
                                "20 Disse-lhe o jovem: Tudo isso tenho guardado; que me falta ainda?%\n" +
                                "21 Disse-lhe Jesus: Se queres ser perfeito, vai, vende tudo o que tens e dá-o aos pobres, e terás um tesouro no céu; e vem, segue-me.%\n" +
                                "22 Mas o jovem, ouvindo essa palavra, retirou-se triste; porque possuía muitos bens.%\n" +
                                "23 Disse então Jesus aos seus discípulos: Em verdade vos digo que um rico dificilmente entrará no reino dos céus.%\n" +
                                "24 E outra vez vos digo que é mais fácil um camelo passar pelo fundo duma agulha, do que entrar um rico no reino de Deus.%\n" +
                                "25 Quando os seus discípulos ouviram isso, ficaram grandemente maravilhados, e perguntaram: Quem pode, então, ser salvo?%\n" +
                                "26 Jesus, fixando neles o olhar, respondeu: Aos homens é isso impossível, mas a Deus tudo é possível.%\n" +
                                "27 Então Pedro, tomando a palavra, disse-lhe: Eis que nós deixamos tudo, e te seguimos; que recompensa, pois, teremos nós?%\n" +
                                "28 Ao que lhe disse Jesus: Em verdade vos digo a vós que me seguistes, que na regeneração, quando o Filho do homem se assentar no trono da sua glória, sentar-vos-eis também vós sobre doze tronos, para julgar as doze tribos de Israel.%\n" +
                                "29 E todo o que tiver deixado casas, ou irmãos, ou irmãs, ou pai, ou mãe, ou filhos, ou terras, por amor do meu nome, receberá cem vezes tanto, e herdará a vida eterna.%\n" +
                                "30 Entretanto, muitos que são primeiros serão últimos; e muitos que são últimos serão primeiros.",

                        "1 Foram ter com Jesus os fariseus, e alguns dos escribas vindos de Jerusalém,%\n" +
                                "2 e repararam que alguns dos seus discípulos comiam pão com as mãos impuras, isto é, por lavar.%\n" +
                                "3 Pois os fariseus, e todos os judeus, guardando a tradição dos anciãos, não comem sem lavar as mãos cuidadosamente;%\n" +
                                "4 e quando voltam do mercado, se não se purificarem, não comem. E muitas outras coisas há que receberam para observar, como a lavagem de copos, de jarros e de vasos de bronze.%\n" +
                                "5 Perguntaram-lhe, pois, os fariseus e os escribas: Por que não andam os teus discípulos conforme a tradição dos anciãos, mas comem o pão com as mãos por lavar?%\n" +
                                "6 Respondeu-lhes: Bem profetizou Isaías acerca de vós, hipócritas, como está escrito: Este povo honra-me com os lábios; o seu coração, porém, está longe de mim;%\n" +
                                "7 mas em vão me adoram, ensinando doutrinas que são preceitos de homens.%\n" +
                                "8 Vós deixais o mandamento de Deus, e vos apegais à tradição dos homens.%\n" +
                                "9 Disse-lhes ainda: Bem sabeis rejeitar o mandamento de Deus, para guardardes a vossa tradição.%\n" +
                                "10 Pois Moisés disse: Honra a teu pai e a tua mãe; e: Quem maldisser ao pai ou à mãe, certamente morrerá.%\n" +
                                "11 Mas vós dizeis: Se um homem disser a seu pai ou a sua mãe: Aquilo que poderias aproveitar de mim é Corbã, isto é, oferta ao Senhor,%\n" +
                                "12 não mais lhe permitis fazer coisa alguma por seu pai ou por sua mãe,%\n" +
                                "13 invalidando assim a palavra de Deus pela vossa tradição que vós transmitistes; também muitas outras coisas semelhantes fazeis.%\n" +
                                "14 E chamando a si outra vez a multidão, disse-lhes: Ouvi-me vós todos, e entendei.%\n" +
                                "15 Nada há fora do homem que, entrando nele, possa contaminá-lo; mas o que sai do homem, isso é que o contamina.%\n" +
                                "16 (Se alguém tem ouvidos para ouvir, ouça.)%\n" +
                                "17 Depois, quando deixou a multidão e entrou em casa, os seus discípulos o interrogaram acerca da parábola.%\n" +
                                "18 Respondeu-lhes ele: Assim também vós estais sem entender? Não compreendeis que tudo o que de fora entra no homem não o pode contaminar,%\n" +
                                "19 porque não lhe entra no coração, mas no ventre, e é lançado fora? Assim declarou puros todos os alimentos.%\n" +
                                "20 E prosseguiu: O que sai do homem , isso é que o contamina.%\n" +
                                "21 Pois é do interior, do coração dos homens, que procedem os maus pensamentos, as prostituições, os furtos, os homicídios, os adultérios,%\n" +
                                "22 a cobiça, as maldades, o dolo, a libertinagem, a inveja, a blasfêmia, a soberba, a insensatez;%\n" +
                                "23 todas estas más coisas procedem de dentro e contaminam o homem.",

                        "11 Porque, se aquilo que se desvanecia era glorioso, muito mais glorioso é o que permanece.%\n" +
                                "12 Tendo, pois, tal esperança, usamos de muita ousadia no falar.%\n" +
                                "13 E não somos como Moisés, que trazia um véu sobre o rosto, para que os filhos de Isra desvanecia;%\n" +
                                "14 mas o entendimento lhes ficou endurecido. Pois até o dia de hoje, à leitura do velho pacto, permanece o mesmo véu, não lhes sendo revelado que em Cristo é ele abolido;%\n" +
                                "15 sim, até o dia de hoje, sempre que Moisés é lido, um véu está posto sobre o coração deles.%\n" +
                                "16 Contudo, convertendo-se um deles ao Senhor, é-lhe tirado o véu.%\n" +
                                "17 Ora, o Senhor é o Espírito; e onde está o Espírito do Senhor aí há liberdade.%\n" +
                                "18 Mas todos nós, com rosto descoberto, refletindo como um espelho a glória do Senhor, somos transformados de glória em glória na mesma imagem, como pelo Espírito do Senhor.\n" +
                                "",

                        "1 E quando alguma pessoa pecar, ouvindo uma voz de blasfêmia, de que for testemunha, seja porque viu, ou porque soube, se o não denunciar, então levará a sua iniqüidade.%\n" +
                                "2 Ou, quando alguma pessoa tocar em alguma coisa imunda, seja corpo morto de fera imunda, seja corpo morto de animal imundo, seja corpo morto de réptil imundo, ainda que não soubesse, contudo será ele imundo e culpado.%\n" +
                                "3 Ou, quando tocar a imundícia de um homem, seja qualquer que for a sua imundícia, com que se faça imundo, e lhe for oculto, e o souber depois, será culpado.%\n" +
                                "4 Ou, quando alguma pessoa jurar, pronunciando temerariamente com os seus lábios, para fazer mal, ou para fazer bem, em tudo o que o homem pronuncia temerariamente com juramento, e lhe for oculto, e o souber depois, culpado será numa destas coisas.%\n" +
                                "5 Será, pois, que, culpado sendo numa destas coisas, confessará aquilo em que pecou.%\n" +
                                "6 E a sua expiação trará ao Senhor, pelo seu pecado que cometeu: uma fêmea de gado miúdo, uma cordeira, ou uma cabrinha pelo pecado; assim o sacerdote por ela fará expiação do seu pecado.%\n" +
                                "7 Mas, se em sua mão não houver recurso para gado miúdo, então trará, para expiação da culpa que cometeu, ao Senhor, duas rolas ou dois pombinhos; um para expiação do pecado, e o outro para holocausto;%\n" +
                                "8 E os trará ao sacerdote, o qual primeiro oferecerá aquele que é para expiação do pecado; e com a sua unha lhe fenderá a cabeça junto ao pescoço, mas não o partirá;%\n" +
                                "9 E do sangue da expiação do pecado espargirá sobre a parede do altar, porém o que sobejar daquele sangue espremer-se-á à base do altar; expiação do pecado é.%\n" +
                                "10 E do outro fará holocausto conforme ao costume; assim o sacerdote por ela fará expiação do seu pecado que cometeu, e ele será perdoado.%\n" +
                                "11 Porém, se em sua mão não houver recurso para duas rolas, ou dois pombinhos, então aquele que pecou trará como oferta a décima parte de um efa de flor de farinha, para expiação do pecado; não deitará sobre ela azeite nem lhe porá em cima o incenso, porquanto é expiação do pecado;%\n" +
                                "12 E a trará ao sacerdote, e o sacerdote dela tomará a sua mão cheia pelo seu memorial, e a queimará sobre o altar, em cima das ofertas queimadas do Senhor; expiação de pecado é.%\n" +
                                "13 Assim o sacerdote por ela fará expiação do seu pecado, que cometeu em alguma destas coisas, e lhe será perdoado; e o restante será do sacerdote, como a oferta de alimentos.%\n" +
                                "14 E falou o Senhor a Moisés, dizendo:%\n" +
                                "15 Quando alguma pessoa cometer uma transgressão, e pecar por ignorância nas coisas sagradas do Senhor, então trará ao Senhor pela expiação, um carneiro sem defeito do rebanho, conforme à tua estimação em siclos de prata, segundo o siclo do santuário, para expiação da culpa.%\n" +
                                "16 Assim restituirá o que pecar nas coisas sagradas, e ainda lhe acrescentará a quinta parte, e a dará ao sacerdote; assim o sacerdote, com o carneiro da expiação, fará expiação por ele, e ser-lhe-á perdoado o pecado.%\n" +
                                "17 E, se alguma pessoa pecar, e fizer, contra algum dos mandamentos do Senhor, aquilo que não se deve fazer, ainda que o não soubesse, contudo será ela culpada, e levará a sua iniqüidade;%\n" +
                                "18 E trará ao sacerdote um carneiro sem defeito do rebanho, conforme à tua estimação, para expiação da culpa, e o sacerdote por ela fará expiação do erro que cometeu sem saber; e ser-lhe-á perdoado.%\n" +
                                "19 Expiação de culpa é; certamente se fez culpado diante do Senhor.\n",

                        "28 Aproximou-se dele um dos escribas que os ouvira discutir e, percebendo que lhes havia respondido bem, perguntou-lhe: Qual é o primeiro de todos os mandamentos?%\n" +
                                "29 Respondeu Jesus: O primeiro é: Ouve, Israel, o Senhor nosso Deus é o único Senhor.%\n" +
                                "30 Amarás, pois, ao Senhor teu Deus de todo o teu coração, de toda a tua alma, de todo o teu entendimento e de todas as tuas forças.\n" };


                ContentValues content_aux = new ContentValues();

                for (int i = 0; i < arrayId.length; i++) {
                        content_aux.put("id", arrayId[i]);
                        content_aux.put("mes", arrayMes[i]);
                        content_aux.put("dia", arrayDia[i]);
                        content_aux.put("titulo", arrayTitulo[i]);
                        content_aux.put("textoBiblico", arrayTexto[i]);

                        db.update("tblleitura_1tri", content_aux, "id = " + arrayId[i], null);
                }

        }
}