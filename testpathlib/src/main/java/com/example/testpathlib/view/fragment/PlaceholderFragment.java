package com.example.testpathlib.view.fragment;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */

import com.example.testpathlib.PathApplication;
import com.example.testpathlib.R;
import com.example.testpathlib.events.Event;
import com.example.testpathlib.jobs.GetDataJob;
import com.path.android.jobqueue.JobManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements View.OnClickListener
{

    private Button btnShow, btnRotate;

    private TextView tvResult;

    private JobManager jobManager = PathApplication.getInstance().getJobManager();

    private String result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        btnShow = (Button) rootView.findViewById(R.id.btnShow);
        btnRotate = (Button) rootView.findViewById(R.id.btnRotate);
        tvResult = (TextView) rootView.findViewById(R.id.tvResult);
        btnShow.setOnClickListener(this);
        btnRotate.setOnClickListener(this);

        // permet de sauvegarder l'instance du fragment de la destruction causé par une rotation de l'écran
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // permet d'indiquer que le fragment est à l'écoute d'événement
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // stope l'écoute d'événement par le fragment
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRotate:
                rotateScreen();
                break;
            case R.id.btnShow:
                getData();
                break;
        }
    }

    /**
     * remet à zéro l'affichage et lance une nouvelle recherche de données
     */
    private void getData()
    {
        tvResult.setText("");
        jobManager.addJobInBackground(1, new GetDataJob());
    }

    /**
     * change l'orientation de l'affichage
     */
    private void rotateScreen()
    {
        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else
        {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void showEditDialog(String txt) {
        FragmentManager fm = getFragmentManager();
        ErrorDialogFragment errorDialog = new ErrorDialogFragment();
        errorDialog.setText(txt);
        errorDialog.show(fm, "fragment_error");
    }

    /**
     * onEventMainThread est la méthode d'EventBus permettant de récupérer
     * des events qui vont affecter l'ui, ici avec tvResult.setText()
     *
     * @param ev le type d'event écouté ici WebDataReceived
     */
    public void onEventMainThread(Event.TextDataReceived ev)
    {
        result = ev.getData();
        tvResult.setText(result);
    }

    /**
     *
     * @param ev le type d'event écouté ici ErrorTextDataReceived
     */
    public void onEventMainThread(Event.ErrorTextDataReceived ev)
    {
        showEditDialog(ev.getData());
    }


}