#pragma version(1)
#pragma rs java_package_name(us.xingkong.visionlibrary)

int width,height;
int r;

void init(){ rsDebug("===init ======",0); }

// rs 的核心函数，每一个像素都会执行这个函数
void root(const uchar4 *v_in, uchar4 *v_out,int x,int y) {
        //rsDebug("===running ======",x);

        int X,Y,n = 0;
        double sumR = 0,sumG = 0,sumB = 0;
        float4 t_color,color;

        for(X = -r;X <= r;X++)
            for(Y = -r;Y <= r;Y++) {
                if (X + x < 0 || X + x >= width || Y + y < 0 || Y + y >= height)
                    continue;
                n++;
                t_color = rsUnpackColor8888(*(v_in + Y * width + X));
                sumR += t_color.r;
                sumG += t_color.g;
                sumB += t_color.b;
            }


        *v_out = rsPackColorTo8888(sumR/n, sumG/n, sumB/n,1);// 将灰度值写入 输入allocation 的当前对应位置点
}
