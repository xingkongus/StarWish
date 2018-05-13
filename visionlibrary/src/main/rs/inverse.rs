#pragma version(1)
#pragma rs java_package_name(us.xingkong.visionlibrary)

void root(const uchar4 *v_in, uchar4 *v_out) {
    float4 f4 = rsUnpackColor8888(*(v_in));
    f4.r = 1 - f4.r;
    f4.g = 1 - f4.g;
    f4.b = 1 - f4.b;
    f4.a = 1;
    *v_out = rsPackColorTo8888(f4);
}
