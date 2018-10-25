package jp.co.mo.logmylife.presentation.view;

import android.support.v4.app.DialogFragment;

public class AbstractDialogFragment extends DialogFragment {

//    public interface Callback {
//        void onDialogSucceeded(int requestCode, int resultCode, Bundle params);
//
//        void onDialogCanceled(int requestCode, Bundle params);
//    }
//
//    /**
//     * Class for making AbstractDialogFragment with Builder pattern.
//     */
//    public static class Builder {
//
//        /** Activity. */
//        final AppCompatActivity mActivity;
//
//        /** 親 Fragment. */
//        final Fragment mParentFragment;
//
//        /** タイトル. */
//        String mTitle;
//
//        /** メッセージ. */
//        String mMessage;
//
//        /** 選択リスト. */
//        String[] mItems;
//
//        /** 肯定ボタン. */
//        String mPositiveLabel;
//
//        /** 否定ボタン. */
//        String mNegativeLabel;
//
//        /** リクエストコード. 親 Fragment 側の戻りで受け取る. */
//        int mRequestCode = -1;
//
//        /** リスナに受け渡す任意のパラメータ. */
//        Bundle mParams;
//
//        /** DialogFragment のタグ. */
//        String mTag = "default";
//
//        /** Dialog をキャンセル可かどうか. */
//        boolean mCancelable = true;
//
//        /**
//         * コンストラクタ. Activity 上から生成する場合.
//         *
//         * @param activity
//         */
//        public <A extends AppCompatActivity & Callback> Builder(@NonNull final A activity) {
//            mActivity = activity;
//            mParentFragment = null;
//        }
//
//        /**
//         * コンストラクタ. Fragment 上から生成する場合.
//         *
//         * @param parentFragment 親 Fragment
//         */
//        public <F extends Fragment & Callback> Builder(@NonNull final F parentFragment) {
//            mParentFragment = parentFragment;
//            mActivity = null;
//        }
//
//        /**
//         * タイトルを設定する.
//         *
//         * @param title タイトル
//         * @return Builder
//         */
//        public Builder title(@NonNull final String title) {
//            mTitle = title;
//            return this;
//        }
//
//        /**
//         * タイトルを設定する.
//         *
//         * @param title タイトル
//         * @return Builder
//         */
//        public Builder title(@StringRes final int title) {
//            return title(getContext().getString(title));
//        }
//
//        /**
//         * メッセージを設定する.
//         *
//         * @param message メッセージ
//         * @return Builder
//         */
//        public Builder message(@NonNull final String message) {
//            mMessage = message;
//            return this;
//        }
//
//        /**
//         * メッセージを設定する.
//         *
//         * @param message メッセージ
//         * @return Builder
//         */
//        public Builder message(@StringRes final int message) {
//            return message(getContext().getString(message));
//        }
//
//        /**
//         * 選択リストを設定する.
//         *
//         * @param items 選択リスト
//         * @return Builder
//         */
//        public Builder items(@NonNull final String... items) {
//            mItems = items;
//            return this;
//        }
//
//        /**
//         * 肯定ボタンを設定する.
//         *
//         * @param positiveLabel 肯定ボタンのラベル
//         * @return Builder
//         */
//        public Builder positive(@NonNull final String positiveLabel) {
//            mPositiveLabel = positiveLabel;
//            return this;
//        }
//
//        /**
//         * 肯定ボタンを設定する.
//         *
//         * @param positiveLabel 肯定ボタンのラベル
//         * @return Builder
//         */
//        public Builder positive(@StringRes final int positiveLabel) {
//            return positive(getContext().getString(positiveLabel));
//        }
//
//        /**
//         * 否定ボタンを設定する.
//         *
//         * @param negativeLabel 否定ボタンのラベル
//         * @return Builder
//         */
//        public Builder negative(@NonNull final String negativeLabel) {
//            mNegativeLabel = negativeLabel;
//            return this;
//        }
//
//        /**
//         * 否定ボタンを設定する.
//         *
//         * @param negativeLabel 否定ボタンのラベル
//         * @return Builder
//         */
//        public Builder negative(@StringRes final int negativeLabel) {
//            return negative(getContext().getString(negativeLabel));
//        }
//
//        /**
//         * リクエストコードを設定する.
//         *
//         * @param requestCode リクエストコード
//         * @return Builder
//         */
//        public Builder requestCode(final int requestCode) {
//            mRequestCode = requestCode;
//            return this;
//        }
//
//        /**
//         * DialogFragment のタグを設定する.
//         *
//         * @param tag タグ
//         * @return Builder
//         */
//        public Builder tag(final String tag) {
//            mTag = tag;
//            return this;
//        }
//
//        /**
//         * Positive / Negative 押下時のリスナに受け渡すパラメータを設定する.
//         *
//         * @param params リスナに受け渡すパラメータ
//         * @return Builder
//         */
//        public Builder params(final Bundle params) {
//            mParams = new Bundle(params);
//            return this;
//        }
//
//        /**
//         * Dialog をキャンセルできるか否かをセットする.
//         *
//         * @param cancelable キャンセル可か否か
//         * @return Builder
//         */
//        public Builder cancelable(final boolean cancelable) {
//            mCancelable = cancelable;
//            return this;
//        }
//
//        /**
//         * DialogFragment を Builder に設定した情報を元に show する.
//         */
//        public void show() {
//            final Bundle args = new Bundle();
//            args.putString("title", mTitle);
//            args.putString("message", mMessage);
//            args.putStringArray("items", mItems);
//            args.putString("positive_label", mPositiveLabel);
//            args.putString("negative_label", mNegativeLabel);
//            args.putBoolean("cancelable", mCancelable);
//            if (mParams != null) {
//                args.putBundle("params", mParams);
//            }
//
//            final AbstractDialogFragment f = new AbstractDialogFragment();
//            if (mParentFragment != null) {
//                f.setTargetFragment(mParentFragment, mRequestCode);
//            } else {
//                args.putInt("request_code", mRequestCode);
//            }
//            f.setArguments(args);
//            if (mParentFragment != null) {
//                f.show(mParentFragment.getChildFragmentManager(), mTag);
//            } else {
//                f.show(mActivity.getSupportFragmentManager(), mTag);
//            }
//        }
//
//        /**
//         * コンテキストを取得する. getString() 呼び出しの為.
//         *
//         * @return Context
//         */
//        private Context getContext() {
//            return (mActivity == null ? mParentFragment.getActivity() : mActivity).getApplicationContext();
//        }
//    }

}
