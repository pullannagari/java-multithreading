package concepts.multithreading;

public class _6_HappensBefore {
    // Happens-before relationship is a concept in java memory model
    // that defines the ordering of memory operations or instructions in a multithreaded environment

    // Happens-before relationship is defined as a set of restrictions on instruction reordering
    // to avoid the instruction reordering that can cause visibility problems in a multithreaded environment.

    // Instruction reordering:
    // CPU's are capable of executing multiple instructions in parallel
    // if the instructions are dependent on each other, they must be executed in order
    // assume the below order of instructions:
    // d = a + b
    // e = c + d
    // f = a + c
    // g = b + c
    // like d = a + b, e = c + d are dependent on each other, so they must be executed in order
    // the other instructions f = a + c, g = b + c are independent, they can be executed in any order
    // so the CPU usually looks ahead of the instructions and reorders them:
    // d = a + b
    // f = a + c
    // e = c + d
    // g = b + c
    // calculating d, f can be done in parallel, so the CPU reorders them
    // Even though CPU reorder them, this will generally be done by JVM or compilers to optimize the code

    // The downside is, this can cause problems in a multithreaded environment

    public class Frame{

    }

    public class FrameExchanger{
        private long framesStoredCount = 0;
        private long framesTakenCount = 0;
        private volatile boolean hasNewFrame = false;
        private Frame frame = null;

        // in the below code, we place a restriction on the cpu
        // instruction reordering by using volatile keyword
        // this is to ensure that frame and framesStoredCount are updated
        // and flushed into the main memory along with the hasNewFrame variable
        // volatile variables are flushed to main memory along with other variables
        // that are updated before volatile variable.
        // This ensures "happens-before" relationship between the variables
        // i.e. the new frame and count is stored before hasNewFrame is set to true
        public void storeFrame(Frame frame){
            this.frame = frame;
            this.framesStoredCount++;
            this.hasNewFrame = true;
        }

        // Happens-before relationship is applicable while reading the volatile variable
        // if a thread reads a volatile variable, it reads the values of the other variables
        // that are flushed along with the volatile variable
        // so in the below code, when a thread reads hasNewFrame as true
        // it also reads the latest values for this.frame and this.framesStoredCount
        public Frame takeFrame(){
            while(!this.hasNewFrame){
                // busy waiting until new frame arrives
            }
            Frame newFrame = this.frame;
            this.framesTakenCount++;
            this.hasNewFrame = false;
            return newFrame;
        }
    }

    // Even synchronized blocks come with happens-before semantics

    public class Values{
        private int valA;
        private int valB;
        private int valC;

        public void setValA(int valA){
            this.valA = valA;
        }

        public void setValB(int valB){
            this.valB = valB;
        }

        public void setValC(int valC){
            this.valC = valC;
        }

        public int getValA(){
            return this.valA;
        }

        public int getValB(){
            return this.valB;
        }

        public int getValC(){
            return this.valC;
        }
    }

    public class ValueExchanger{
        private int valA;
        private int valB;
        private int valC;

        // when a thread exits a synchronized block, it writes the values of the variables
        // within the synchronized block to the main memory
        // all the changes made within the synchronized block and before the synchronized block
        // cannot be reordered by the CPU
        public void set(Values v){
            synchronized (this){
                this.valA = v.getValA();
                this.valB = v.getValB();
                this.valC = v.getValC();
            }
        }

        // when a thread enters a synchronized block, it reads the values of the variables
        // within the synchronized block from the main memory
        public void get(Values v){
            synchronized (this){
                v.valA = this.valA;
                v.valB = this.valB;
                v.valC = this.valC;
            }
        }

        // A write that happens before we exit a synchronized block, cannot be reordered
        // to happen after we exit the synchronized block
        // A read that happens after we enter a synchronized block, cannot be reordered
        // to happen before we enter the synchronized block

        // Additional Nuances:
        // End of synchronized block happens-before the start of the next synchronized block on the same monitor
        // The start of a thread happens-before the thread's run method
        // There are several happens before guarantees in java.util.concurrent package

        // overall happens-before guarantee:
        // A set of restrictions on "instruction reordering" to avoid "instruction reordering" breaking java visibility
    }



}
