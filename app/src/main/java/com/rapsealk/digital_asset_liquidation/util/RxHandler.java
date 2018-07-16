package com.rapsealk.digital_asset_liquidation.util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import io.reactivex.MaybeEmitter;
import io.reactivex.annotations.NonNull;

/**
 * Created by rapsealk on 2018. 7. 17..
 */
public class RxHandler<T> implements OnSuccessListener<T>, OnFailureListener, OnCompleteListener<T> {

    private final MaybeEmitter<? super T> emitter;

    private RxHandler(MaybeEmitter<? super T> emitter) {
        this.emitter = emitter;
    }

    public static <T> void assignOnTask(MaybeEmitter<? super T> emitter, Task<T> task) {
        RxHandler handler = new RxHandler(emitter);
        task.addOnSuccessListener(handler);
        task.addOnFailureListener(handler);
        try {
            task.addOnCompleteListener(handler);
        } catch (Throwable t) {

        }
    }

    @Override
    public void onSuccess(T result) {
        if (result != null) {
            emitter.onSuccess(result);
        } else {
            emitter.onError(new Exception("Observable cannot emit null value."));
        }
    }

    @Override
    public void onComplete(@NonNull Task<T> task) {
        emitter.onComplete();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (!emitter.isDisposed()) {
            emitter.onError(e);
        }
    }
}
