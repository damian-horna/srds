package cassdemo;

public class Customer  implements Runnable{
    public int id;
    public int selectedUseCase;

    public Customer(int id, int selectedUseCase) {
        this.id = id;
        this.selectedUseCase = selectedUseCase;
    }

    @Override
    public void run() {
        switch (this.selectedUseCase){
            case 1:
                useCase1();
                break;
            case 2:
                useCase2();
                break;
            case 3:
                useCase3();
                break;
        }
    }

    public void useCase1(){
        System.out.printf("Customer %d doing use case 1\n", this.id);
    }

    public void useCase2(){
        System.out.printf("Customer %d doing use case 2\n", this.id);
    }

    public void useCase3(){
        System.out.printf("Customer %d doing use case 3\n", this.id);
    }

}
