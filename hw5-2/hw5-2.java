import edu.princeton.cs.algs4.MinPQ;

class Plan implements Comparable<Plan>{
    int NumberOfTraveller, FromCity, ToCity, DateOfDeparture, DateOfArrival, Event;
    public Plan(int NumberOfTraveller, int FromCity, int ToCity, int DateOfDeparture, int DateOfArrival, int Event) {
        this.NumberOfTraveller = NumberOfTraveller;
        this.FromCity = FromCity;
        this.ToCity = ToCity;
        this.DateOfDeparture = DateOfDeparture;
        this.DateOfArrival = DateOfArrival;
        this.Event = Event;
    }

    @Override
    public int compareTo(Plan o) {
        int cmp = this.DateOfDeparture - o.DateOfDeparture;
        if (cmp != 0) {
            return cmp;
        }
        return this.Event - o.Event;
    }
}

class CovidSimulation {
    MinPQ<Plan> plans = new MinPQ<>();
    int[] numOfCitizen;
    int[] recoveryDates;
    int[] maxRecoveryDates;

    public CovidSimulation(int[] Num_Of_Citizen) {
        numOfCitizen = Num_Of_Citizen;
        int numOfCity = numOfCitizen.length;
        recoveryDates = new int[numOfCity];
        maxRecoveryDates = new int[numOfCity];

        for (int i = 0; i < numOfCitizen.length; i++) {
            recoveryDates[i] = 0;
            maxRecoveryDates[i] = 0;
        }
    }

    private int findMostPatient(int date) {
        int max = -1;
        for (int i = 0; i < numOfCitizen.length; i++) {
            if (date < recoveryDates[i] && date >= maxRecoveryDates[i]-7) {
                if (max == -1) {
                    max = i;
                }
                else if (numOfCitizen[i] >= numOfCitizen[max]) {
                    max = i;
                }
            }
        }
        return max;
    }

    public int CityWithTheMostPatient(int date) {
        while (!plans.isEmpty() && plans.min().DateOfDeparture <=date) {
            Plan plan = plans.delMin();

            if (plan.Event == 0) { //virus
                int day = plan.DateOfDeparture;
                int city = plan.FromCity;

                if (day >= maxRecoveryDates[city]-7 && day < recoveryDates[city]) continue; // the city is infected, so the virus's attack is in vain.

                recoveryDates[city] = day+4;
                maxRecoveryDates[city] = day+7;
            }
            else if (plan.Event == 1) { // traveler leave
                int numberOfTraveller = plan.NumberOfTraveller;
                int dateOfDeparture = plan.DateOfDeparture;
                int dateOfArrival = plan.DateOfArrival;
                int fromCity = plan.FromCity;
                int toCity = plan.ToCity;

                if (dateOfDeparture >= maxRecoveryDates[fromCity]-7 && dateOfDeparture < recoveryDates[fromCity] && dateOfArrival < recoveryDates[fromCity]) { // depart and arrive with virus
                    if (dateOfArrival >= maxRecoveryDates[toCity]-7 && dateOfArrival < recoveryDates[toCity]) {
                        recoveryDates[toCity] = Math.min(Math.max(recoveryDates[fromCity], recoveryDates[toCity]), maxRecoveryDates[toCity]);
                    }
                    else {
                        recoveryDates[toCity] = dateOfArrival+4;
                        maxRecoveryDates[toCity] = dateOfArrival+7;
                    }
                }
                numOfCitizen[fromCity] -= numberOfTraveller;
            }
            else if (plan.Event == 2) {
                int numberOfTraveller = plan.NumberOfTraveller;
                int city = plan.FromCity;

                numOfCitizen[city] += numberOfTraveller;
            }
        }

        return findMostPatient(date);
    }

    public void virusAttackPlan(int city, int date) {
        Plan plan = new Plan(-1, city, -1, date, -1,0);
        plans.insert(plan);
    }

    public void TravelPlan(int NumberOfTraveller, int FromCity, int ToCity, int DateOfDeparture, int DateOfArrival) {
        Plan leavePlan = new Plan(NumberOfTraveller, FromCity, ToCity, DateOfDeparture, DateOfArrival, 1);
        plans.insert(leavePlan);
        Plan arrivePlan = new Plan(NumberOfTraveller, ToCity, -1, DateOfArrival, -1, 2);
        plans.insert(arrivePlan);
    }
}
