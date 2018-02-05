package org.amv.access.sdk.sample.logic;

import android.content.Context;

import org.amv.access.sdk.spi.certificate.AccessCertificatePair;
import org.amv.access.sdk.spi.certificate.CertificateManager;
import org.amv.access.sdk.spi.certificate.DeviceCertificate;
import org.amv.access.sdk.spi.error.AccessSdkException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class CertificatesController implements ICertificatesController {

    private ICertificatesView view;
    private volatile CertificateManager certificateManager;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public void initialize(ICertificatesView view, Context context) {
        checkNotNull(context);
        this.view = checkNotNull(view);

        AmvSdkInitializer.create(context.getApplicationContext())
                // add artificial delay
                .delay(333, TimeUnit.MILLISECONDS)
                .doOnNext(sdk -> this.certificateManager = sdk.certificateManager())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foo -> {
                    this.initialized.set(true);

                    view.onInitializeFinished();
                }, e -> {
                    view.onInitializeFailed(AccessSdkException.wrap(e));
                });
    }


    @Override
    public Observable<DeviceCertificate> getDeviceCertificate() {
        isInitializedOrThrow();

        return certificateManager.getDeviceCertificate()
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<AccessCertificatePair> getAccessCertificates() {
        isInitializedOrThrow();

        return certificateManager.getAccessCertificates()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void downloadCertificates() {
        isInitializedOrThrow();

        certificateManager.refreshAccessCertificates()
                // add a delay to simulate real network
                .delay(500, TimeUnit.MILLISECONDS)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(certs -> {
                    view.onCertificatesDownloaded(certs);
                }, e -> {
                    view.onCertificatesDownloadFailed(AccessSdkException.wrap(e));
                });
    }

    @Override
    public void revokeCertificate(AccessCertificatePair accessCertificatePair) {
        checkNotNull(accessCertificatePair);

        isInitializedOrThrow();

        certificateManager.revokeAccessCertificate(accessCertificatePair)
                .flatMap(foo -> certificateManager.getAccessCertificates())
                // add a delay to simulate real network
                .delay(500, TimeUnit.MILLISECONDS)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accessCertificates -> {
                    view.onCertificateRevoked(accessCertificates);
                }, e -> {
                    view.onCertificateRevokeFailed(AccessSdkException.wrap(e));
                });
    }


    private void isInitializedOrThrow() {
        if (!initialized.get() || certificateManager == null) {
            throw new IllegalStateException("Call `initialize` and wait for `onInitializeFinished` callback");
        }
    }
}
