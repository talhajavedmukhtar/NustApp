package nust.orientationapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Talha on 9/17/16.
 */
public class Places {
    public Places(){}

    public String[] institutions = {
            "School of Electrical Engineering and Computer Science",
            "School of Civil and Environmental Engineering",
            "School of Chemical and Materials Engineering",
            "NUST Business School",
            "School of Art, Design and Architechture",
            "School of Mechanical and Manufacturing Engineering",
            "School of Natural Sciences",
            "School of Social Sciences and Humanities",
            "Atta-ur-Rehman School of Applied Biosciences",
            "Institute of Applied Electronics and Computing",
            "Research Center for Microwave and Millimeter Wave Studies",
            "Institute of Geographical Information Systems",
            "Institute of Environmental Science and Engineering",
            "NUST Institute of Civil Engineering"
    };

    public LatLng[] institutionsL = {
            new LatLng(33.642714,72.990433),
            new LatLng(33.640502,72.984672),
            new LatLng(33.648982,72.992879),
            new LatLng(33.644039,72.991384),
            new LatLng(33.646021,72.988779),
            new LatLng(33.636291,72.989391),
            new LatLng(33.636861,72.990176),
            new LatLng(33.644243,72.993017),
            new LatLng(33.646370,72.987776),
            new LatLng(33.644173,72.987819),
            new LatLng(33.644481,72.987095),
            new LatLng(33.644967,72.988130),
            new LatLng(33.647822,72.989767),
            new LatLng(33.640698,72.985139)
    };

    public String[] cafes = {
            "Concordia-1",
            "Concordia-2",
            "Concordia-3"
    };

    public LatLng[] cafesL = {
            new LatLng(33.646473, 72.990149),
            new LatLng(33.643032, 72.988064),
            new LatLng(33.641839, 72.993812)
    };

    public String[] mosques = {
            "Masjid Rehmat"
    };

    public LatLng[] mosquesL = {
            new LatLng(33.644090, 72.985719)
    };

    public String[] banks = {
            "Habib Bank Limited Bank and ATM",
            "HBL ATM",
            "HBL ATM"
    };

    public LatLng[] banksL = {
            new LatLng(33.643260, 72.984814),
            new LatLng(33.646473, 72.990149),
            new LatLng(33.643032, 72.988064)
    };

    public String[] girlsHostels = {
            "Zainab Hostel",
            "Ayesha Hostel"
    };

    public LatLng[] girlsHostelsL = {
            new LatLng(33.645465, 72.993863),
            new LatLng(33.645025, 72.994456)
    };

    public String[] boysHostels = {
            "Ghazali-1 Hostel",
            "Ghazali-2 Hostel",
            "Attar-1 Hostel",
            "Attar-2 Hostel",
            "Razi-1 Hostel",
            "Razi-2 Hostel"
    };

    public LatLng[] boysHostelsL = {
            new LatLng(33.640155, 72.987599),
            new LatLng(33.640544, 72.987300),
            new LatLng(33.639768, 72.988645),
            new LatLng(33.639962, 72.988905),
            new LatLng(33.639707, 72.986852),
            new LatLng(33.640067, 72.986578)
    };

    public String[] gyms = {
            "NUST Gym",
            "Ghazali-1 Gym",
            "Ghazali-2 Gym",
            "Attar-1 Gym",
            "Attar-2 Gym",
            "Razi-1 Gym",
            "Razi-2 Gym"
    };

    public LatLng[] gymsL = {
            new LatLng(33.644934, 72.993081),
            new LatLng(33.640155, 72.987599),
            new LatLng(33.640544, 72.987300),
            new LatLng(33.639768, 72.988645),
            new LatLng(33.639962, 72.988905),
            new LatLng(33.639707, 72.986852),
            new LatLng(33.640067, 72.986578)
    };

    public String[] others = {
            "Centre for International Peace and Stability",
            "NUST Main Office"
    };

    public LatLng[] othersL = {
            new LatLng(33.643214, 72.993144),
            new LatLng(33.642415, 72.992962)
    };

    public String[] getNames(int pos){
        switch (pos){
            case 0:
                return institutions;
            case 1:
                return cafes;
            case 2:
                return mosques;
            case 3:
                return banks;
            case 4:
                return girlsHostels;
            case 5:
                return boysHostels;
            case 6:
                return gyms;
            case 7:
                return others;
            default:
                return institutions;
        }
    }

    public LatLng[] getLocations(int pos){
        switch (pos){
            case 0:
                return institutionsL;
            case 1:
                return cafesL;
            case 2:
                return mosquesL;
            case 3:
                return banksL;
            case 4:
                return girlsHostelsL;
            case 5:
                return boysHostelsL;
            case 6:
                return gymsL;
            case 7:
                return othersL;
            default:
                return institutionsL;
        }
    }

}
