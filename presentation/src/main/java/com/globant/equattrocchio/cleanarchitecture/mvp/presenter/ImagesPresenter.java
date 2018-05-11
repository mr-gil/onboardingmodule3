package com.globant.equattrocchio.cleanarchitecture.mvp.presenter;

import android.app.Activity;

import com.globant.equattrocchio.cleanarchitecture.util.bus.RxBus;
import com.globant.equattrocchio.cleanarchitecture.mvp.view.ImagesView;
import com.globant.equattrocchio.cleanarchitecture.util.bus.observers.CallServiceButtonObserver;
import com.globant.equattrocchio.data.ImagesServicesImpl;
import com.globant.equattrocchio.domain.GetLatestImagesUseCase;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;

public class ImagesPresenter {

    private ImagesView view;
    private GetLatestImagesUseCase getLatestImagesUseCase;

    public ImagesPresenter(ImagesView view, GetLatestImagesUseCase getLatestImagesUseCase) {
        this.view = view;
        this.getLatestImagesUseCase = getLatestImagesUseCase;
    }

    public void onCountButtonPressed() {
        view.showText(new String(""));//todo: aca va el string que me devuelva el execute del usecase
    }

    private void onCallServiceButtonPressed() {

        getLatestImagesUseCase.execute(new DisposableObserver<String>() {

            @Override
            public void onNext(@NonNull String result) {
            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.showError();
            }

            @Override
            public void onComplete() {
                new ImagesServicesImpl().getLatestImages(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        view.showText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
            }
        }, null);

        //todo acá tengo que llamar a la domain layer para que llame a la data layer y haga el llamdo al servicio
    }

    private void loadFromPreferences() {
        // view.showText("EL TEXTO QUE ME TRAGIA DE LAS PREFERENCES");// todo: traerme el texto de las preferences
    }

    public void register() {
        Activity activity = view.getActivity();
        if (activity != null) {
            RxBus.subscribe(activity, new CallServiceButtonObserver() {
                @Override
                public void onEvent(CallServiceButtonPressed event) {
                    onCallServiceButtonPressed();
                }
            });
        }
    }

    public void unregister() {
        Activity activity = view.getActivity();
        if (activity != null) {
            RxBus.clear(activity);
        }
    }
}
