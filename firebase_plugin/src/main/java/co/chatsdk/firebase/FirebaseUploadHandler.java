package co.chatsdk.firebase;

import android.support.annotation.NonNull;

import co.chatsdk.core.defines.Debug;
import com.braunster.chatsdk.dao.core.DaoCore;
import com.braunster.chatsdk.network.BDefines;
import com.braunster.chatsdk.object.ChatError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import co.chatsdk.core.handlers.UploadHandler;
import co.chatsdk.core.types.FileUploadResult;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import timber.log.Timber;

/**
 * Created by Erk on 26.07.2016.
 */
public class FirebaseUploadHandler implements UploadHandler {

    private static final String TAG = FirebaseUploadHandler.class.getSimpleName();
    private static final boolean DEBUG = Debug.BFirebaseUploadHandler;

    // TODO: HOT
    @Override
    public Observable<FileUploadResult> uploadFile(final byte[] data, final String name, final String mimeType) {
        return Observable.create(new ObservableOnSubscribe<FileUploadResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FileUploadResult> e) throws Exception {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(BDefines.FirebaseStoragePath);
                StorageReference filesRef = storageRef.child("files");
                final String fullName = getUUID() + "_" + name;
                StorageReference fileRef = filesRef.child(fullName);

                final FileUploadResult result = new FileUploadResult();

                UploadTask uploadTask = fileRef.putBytes(data);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        result.name = name;
                        result.mimeType = mimeType;
                        result.url = taskSnapshot.getDownloadUrl().toString();
                        e.onNext(result);
                        e.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception error) {
                        if (DEBUG) Timber.e(error.getCause(), "Firebase storage exception while saving");
                        e.onError(ChatError.getError(ChatError.Code.FIREBASE_STORAGE_EXCEPTION, error.getMessage()));
                    }
                });

            }
        });
    }

    private String getUUID() {
        return DaoCore.generateEntity();
    }
}
