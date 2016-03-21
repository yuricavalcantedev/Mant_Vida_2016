package yuri.com.br.mant_vida_2016;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yuri on 23/12/2015.
 */

public class PushNotificationService extends Service {

    public static final String NOTIFICATION_INTENT = "NOTIFICATION_INTENT";
    public static final String FROM_NOTIFICATION_EXTRA_KEY = "from_notification";
    private Calendar calendar = Calendar.getInstance();
    private String titulo;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        gerarNotificacao();

        cancelPreviousIntent();

        sendBroadcast(new Intent (this,AlarmReceiver.class));

        stopSelf();
    }

    private void gerarNotificacao() {

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        String leitura = "";
        ArrayList<ClassTblLeitura> versiclesArray = new ArrayList<>();

        String nomeUsuario = databaseAccess.getUserName();
        versiclesArray = databaseAccess.buscarTextosDia(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        databaseAccess.close();

        //Se não setar o titulo como vazio toda vida ele vai ficar acumulando sempre que apertar algum item do gridView.
        titulo = "";

        Intent intentLer = new Intent(this, Reading.class);

        int countForAux = 0;
        int tamanhoVersicleArray = versiclesArray.size();

        //Guardo em "leitura", todos os títulos que vinheram do banco de dados.
        while (tamanhoVersicleArray > 0) {

            String[] versiclesSplit = versiclesArray.get(countForAux).getTextoBiblico().split("%");

            leitura += versiclesArray.get(0).getTitulo() + ", ";

            intentLer.putExtra("titulo" + countForAux, versiclesArray.get(countForAux).getTitulo());
            intentLer.putExtra("textoBiblico" + countForAux, versiclesSplit);

            countForAux++;
            tamanhoVersicleArray--;

        }

        for (int i = 0; i < countForAux; i++) {

            titulo += versiclesArray.get(i).getTitulo() + "\n ";

        }

        //Coloca na intent o número de leitura que exitem no dia.
        //Serve para a lógica dos botões.
        intentLer.putExtra("qntdLeituras", countForAux);

        //Passa esses dados para a activity da leitura, de lá, vai ser passado para a activity do devocional.

        intentLer.putExtra("Leitura", titulo);
        intentLer.putExtra("Parasha", selectParashaName(calendar.get(Calendar.WEEK_OF_YEAR)));
        intentLer.putExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
        intentLer.putExtra("month", calendar.get(Calendar.MONTH));
        intentLer.putExtra(PushNotificationService.FROM_NOTIFICATION_EXTRA_KEY,true);


        String[] mensagemAlerta = new String[]{"Shalom " + nomeUsuario + "!", "Leitura diária: " + leitura,"Escondi tua palavra no meu coração para não pecar contra Ti. Salmo 199:11"};

        //NÃO ENTENDI DIREITO
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        intentLer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi_ler = PendingIntent.getActivity(this,0,intentLer,PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentAdiar = new Intent(this, PlanoLeitura.class);
        intentAdiar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAdiar.putExtra(PushNotificationService.FROM_NOTIFICATION_EXTRA_KEY,true);
        PendingIntent pi_adiar = PendingIntent.getActivity(this,0,intentAdiar,PendingIntent.FLAG_CANCEL_CURRENT);

        android.support.v7.app.NotificationCompat.InboxStyle style = new android.support.v7.app.NotificationCompat.InboxStyle();
        for (int i = 0; i < mensagemAlerta.length; i++) {
            style.addLine(mensagemAlerta[i]);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this)
                // Show controls on lock screen even when user hides sensitive content.
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.lg_icant72x72)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.lg_icant72x72))
                        // Add media control buttons that invoke intents in your media service
                .addAction(R.drawable.ic_cancel_white_24dp, "Adiar", pi_adiar)  // #1
                .addAction(R.drawable.ic_done_white_24dp, "Ler", pi_ler)     // #2
                        // Apply the media style template
                .setContentTitle("MANT VIDA")
                .setTicker("MANT VIDA")
                .setContentText(mensagemAlerta[1])
                .setStyle(style)
                .setSound(soundUri)
                .build();


        notification.vibrate = new long[]{150, 300, 150, 600};
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.mipmap.lg_icant72x72, notification);

    }

    private void cancelPreviousIntent(){

        PendingIntent pi = PendingIntent.getService(PushNotificationService.this,0,new Intent(NOTIFICATION_INTENT),PendingIntent.FLAG_NO_CREATE);
        if(pi != null)
            pi.cancel();

    }

    private String selectParashaName(int WEEK_OF_YEAR) {

        String[] parashas =
                {       "Parashá 12 - Vayêchí (E viveu)",
                        "Parashá 13 - Shêmot (Nomes)", "Parashá 14 - Vaêrá (E apareci)", "Parashá 15 - Bô (Vá)",
                        "Parashá 16 - Bêshalách (Ao enviar)", "Parashá 17 - Ytrô (Jetro)", "Parashá 18 - Mishpatim (Juizos)",
                        "Parashá 19 - Têrumah (oferta)", "Parashá 20 - Têtsávêh (Ordena!)", "Parashá 21 - Ki tissá (Quando realizar)",
                        "Parashá 22 - Vayakêhel (E reuniu); Parashá 23 - Pêkudei (Registro)", "Parashá 24 - Vayakrá (Ele chamou",
                        "Parashá 25 - Tsav (Ordene!)", "Parashá 26 - Shêmini (Oitavo)",
                        "Parashá 27 - Tazria (Conceber); Parashá 28 - Mêtsôrá (leproso)", "Parashá 29 - Acharêi môt (Depois da morte); Parashá 30 - Kêdoshim (Santos)",
                        "Parashá 31 - Êmor (Fale!)","Pêsach (Festa da Páscoa)", "Parashá 32 - Bêhar (No monte)", "Parashá 33 - Bêchukôtai (Nos meus estatutos)",
                        "Parashá 34 - Bêmidbar (No deserto)", "Parashá 35 - Násô (Levanta)", "Parashá 36 - Bêháalôtêchá (Quando acenderes)", "Parashá 37 - Shlách lêchá (Envie para você)",
                        "Parashá 38 - Kôrách (Coré)", "Parashá 39 - Chukat (Estatutos)", "Parashá 40 - Balák (Balaque)", "Parashá 41 - Pinchás (Finéias)",
                        "Parashá 42 - Matôt (Tribos); Parashá 43 - Masêi (Jornadas)", "Parashá 44 - Dêvarim (דברים) (Palavras)", "Parashá 45 - Vaêtchanan (Supliquei)",
                        "Parashá 46 - Êkev (Por causa)", "Parashá 47 - Rêeh (Veja!)", "Parashá 48 - Shôftim (Juízes)",
                        "Parashá 49 - Ki têtsê (Quando saíres)", "Parashá 50 - Ki tavô (Quando vieres)", "Parashá 51 - Nitzavim (Encontram-se)",
                        "Parashá 52 - Vayeléch (E Ele foi)", "Parashá 53 - Háazinu (Ouçam)", "Parashá 54 - Vêzot HaBracháh (Esta é a Bênção)",
                        "Parashá 1 - Bêreshit ((בראשית) No Princípio)", "Parashá 2 - Nôach (Noé)","Sokôt (Festa dos Tabernáculos)", "Parashá 3 - Lêch lêchá (Sai para ti",
                        "Parashá 4 - Vayêrá (E apareceu)", "Parashá 5 - Chayê Sarah (A vida de Sara)", "Parashá 6 - Tôlêdôt (Gerações)",
                        "Parashá 7 - Vayêtsê (E saiu)", "Parashá 8 - Vaishlách (E enviou)", "Parashá 9 - Vayêshev (E habitou)",
                        "Parashá 10 - Miquêts (Ao fim)", "Parashá 11 - Vaygásh (E aproximou-se)",
                        "Parashá 12 - Vayêchí (E viveu)"
                };

        switch (WEEK_OF_YEAR) {

            case 1:
                return parashas[0];
            case 2:
                return parashas[1];
            case 3:
                return parashas[2];
            case 4:
                return parashas[3];
            case 5:
                return parashas[4];
            case 6:
                return parashas[5];
            case 7:
                return parashas[6];
            case 8:
                return parashas[7];
            case 9:
                return parashas[8];
            case 10:
                return parashas[9];
            case 11:
                return parashas[10];
            case 12:
                return parashas[11];
            case 13:
                return parashas[12];
            case 14:
                return parashas[13];
            case 15:
                return parashas[14];
            case 16:
                return parashas[15];
            case 17:
                return parashas[16];
            case 18:
                return parashas[17];
            case 19:
                return parashas[18];
            case 20:
                return parashas[19];
            case 21:
                return parashas[20];
            case 22:
                return parashas[21];
            case 23:
                return parashas[22];
            case 24:
                return parashas[23];
            case 25:
                return parashas[24];
            case 26:
                return parashas[25];
            case 27:
                return parashas[26];
            case 28:
                return parashas[27];
            case 29:
                return parashas[28];
            case 30:
                return parashas[29];
            case 31:
                return parashas[30];
            case 32:
                return parashas[31];
            case 33:
                return parashas[32];
            case 34:
                return parashas[33];
            case 35:
                return parashas[34];
            case 36:
                return parashas[35];
            case 37:
                return parashas[36];
            case 38:
                return parashas[37];
            case 39:
                return parashas[38];
            case 40:
                return parashas[39];
            case 41:
                return parashas[40];
            case 42:
                return parashas[41];
            case 43:
                return parashas[42];
            case 44:
                return parashas[43];
            case 45:
                return parashas[44];
            case 46:
                return parashas[45];
            case 47:
                return parashas[46];
            case 48:
                return parashas[47];
            case 49:
                return parashas[48];
            case 50:
                return parashas[49];
            case 51:
                return parashas[50];
            case 52:
                return parashas[51];
            case 53:
                return parashas[52];
            case 54:
                return parashas[53];


        }

        return "";
    }


}
