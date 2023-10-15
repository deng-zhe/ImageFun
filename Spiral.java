import java.util.Stack;

public class Spiral {
    // find selectd object by traverse the array in traverse order from the pick point
    //set the touched pixel with 2
    public int x;
    public int y;
    public int[][] borderAry;
    public int Width;
    public int Height;
    public int blksize;
    Stack<Integer> stk;
    public Spiral(int[][] ary,int i,int j)
    {
        blksize=100;
        borderAry=ary;
        Width=ary.length;
        Height=ary[0].length;
        x=i;
        y=j;
        stk= new Stack<Integer>();
        stk.push(x);
        stk.push(y);
    }
    public int[][] Infect()
    // A fast and exact algorithms
    {
        int X,Y;
        while(!stk.isEmpty())
        {
            Y=stk.pop();
            X=stk.pop();
            if(X>=0 && X<Width && Y>=0 && Y<Height) {
                if (borderAry[X][Y] == 1)
                {
                    borderAry[X][Y]=2;
                    stk.push(X+1);
                    stk.push(Y);
                    stk.push(X-1);
                    stk.push(Y);
                    stk.push(X);
                    stk.push(Y+1);
                    stk.push(X);
                    stk.push(Y-1);
                }
            }
        }
        return borderAry;
    }
    public int[][] run()
    {
       boolean done=false;
        int L=1,R=1,U=1,D=1;
        int ofstx=x,ofsty=y;
        borderAry[x][y]=2;
        while(!done)
        {
            done=true;
            int L1,R1,U1,D1;
            R1=x+R;
            if (R1 > Width-2) R1=Width-2;
            while (ofstx<R1)
            {
                ofstx+=1;
                if(borderAry[ofstx][ofsty]==1 && (borderAry[ofstx-1][ofsty]==2 || borderAry[ofstx][ofsty-1]==2 || borderAry[ofstx-1][ofsty-1]==2 || borderAry[ofstx+1][ofsty-1]==2))
                {
                    borderAry[ofstx][ofsty]=2;
                    done=false;
                }
            }
            R+=1;
            D1=y+D;
            if (D1 > Height-2) D1=Height-2;
            while (ofsty < D1 )
            {
                ofsty+=1;
                if(borderAry[ofstx][ofsty]==1 && (borderAry[ofstx][ofsty-1]==2 || borderAry[ofstx-1][ofsty]==2 || borderAry[ofstx-1][ofsty-1]==2 || borderAry[ofstx-1][ofsty+1]==2))
                {
                    borderAry[ofstx][ofsty]=2;
                    done=false;
                }
            }
            D+=1;
            L1=x-L;
            if(L1 < 1) L1=1;
            while (ofstx > L1 )
            {
                ofstx-=1;
                if(borderAry[ofstx][ofsty]==1 && (borderAry[ofstx+1][ofsty]==2 || borderAry[ofstx-1][ofsty-1]==2 || borderAry[ofstx][ofsty-1]==2 || borderAry[ofstx+1][ofsty-1]==2 ))
                {
                    borderAry[ofstx][ofsty]=2;
                    done=false;
                }
            }
            L+=1;
            U1=y-U;
            if(U1<1) U1=1;
            while (ofsty > U1 )
            {
                ofsty-=1;
                if(borderAry[ofstx][ofsty]==1 && (borderAry[ofstx][ofsty+1]==2 || borderAry[ofstx+1][ofsty-1]==2 || borderAry[ofstx+1][ofsty]==2 || borderAry[ofstx+1][ofsty+1]==2))
                {
                    borderAry[ofstx][ofsty]=2;
                    done=false;
                }
            }
            U+=1;
        }
        return borderAry;
    }

}
