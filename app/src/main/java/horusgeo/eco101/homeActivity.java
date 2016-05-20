package horusgeo.eco101;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Início");


        Intent intent = getIntent();
        String nome = intent.getStringExtra("PersonID");

        TextView t = (TextView) findViewById(R.id.textoInicio);


        t.setText("Bem vindo, " + nome + "! Por favor, selecione uma opção a seguir:\n\n");

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.opcoesSessao);
        radioGroup.requestFocus();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            TextView explain = (TextView) findViewById(R.id.explainText);
            TextView warning = (TextView) findViewById(R.id.warningText);
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.novaSessao){
                    explain.setText("\nIniciar uma nova sessão.");
                    warning.setText("ATENÇÃO! É necessário conexão com internet!\n");
                }
                if(checkedId == R.id.continuarSessao){
                    explain.setText("\nContinuar uma sessão já carregada.");
                    warning.setText("\n");
                }
                if(checkedId == R.id.encerrarSessao){
                    explain.setText("\nEncerrar uma sessão e enviar os dados para o servidor.");
                    warning.setText("ATENÇÃO! É necessário conexão com internet!\n");
                }
                if(checkedId == R.id.limparSessao){
                    explain.setText("\nApagar uma sessão.");
                    warning.setText("ATENÇÃO! Certifique-se de ter enviado os dados da sessão antes de apagar!\n");
                }
            }
        });

        Button continueButton = (Button) findViewById(R.id.buttonContinuar);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNext(radioGroup.getCheckedRadioButtonId());
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void goNext(int radioId){
        Intent intent;
        if(radioId == R.id.novaSessao){
            intent = new Intent(this, NewSessionActivity.class);
            startActivity(intent);
        }
        if(radioId == R.id.continuarSessao){
            intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        if(radioId == R.id.encerrarSessao){
//            intent = new Intent(this, closeSessionActivity.class);
//            startActivity(intent);
        }
        if(radioId == R.id.limparSessao){
//            intent = new Intent(this, clearSessionActivity.class);
//            startActivity(intent);
        }

    }

}
