package com.example.firebase_lawyer;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdap extends FragmentStateAdapter {
    String lawyerID;
    public ViewPagerAdap(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerID = lawyerId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        String lawyerId = this.lawyerID;
        switch(position){
            case 0:
                fragment= new Gigs();
                break;
            case 1:
                fragment = new Pending();
                break;
            case 2:
                fragment= new Profile();
                break;
            default:
                fragment= new Gigs();
                break;
        }

        if (fragment instanceof Gigs) {
            ((Gigs) fragment).setLawyerId(lawyerId);
        } else if (fragment instanceof Profile) {
            ((Profile) fragment).setLawyerId(lawyerId);
        } else if (fragment instanceof Pending) {
            ((Pending) fragment).setLawyerId(lawyerId);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}