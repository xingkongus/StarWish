package us.xingkong.visionlibrary.operator;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vision.image.VisionImage;

/**
 * 图像操作抽象类
 * <p>
 * Created by 饶翰新 on 2018/4/13.
 */

public abstract class ImageOperator {

    /**
     * 参数解释
     */
    private Map<String, String> argText;

    public ImageOperator() {
        argText = new HashMap<>();
    }

    /**
     * 处理图像
     *
     * @param img 原图
     */
    public abstract void Operator(VisionImage img, Context context);


    /**
     * 获取参数名和类型
     *
     * @return
     * @throws IllegalAccessException
     */
    public Map<String, Class> getArgs() throws IllegalAccessException {
        Map<String, Class> map = new HashMap<>();
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(this).getClass());
        }
        return map;
    }

    /**
     * 设置参数值
     *
     * @param name  参数名
     * @param value 参数值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public void setArg(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        this.getClass().getField(name).set(this, value);
    }

    /**
     * 获取参数值
     *
     * @param name 参数名
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public Object getArg(String name) throws NoSuchFieldException, IllegalAccessException {
        return this.getClass().getField(name).get(this);
    }

    /**
     * 获取参数文字
     *
     * @param name 参数名
     * @return
     */
    public String getArgText(String name) {
        String text = argText.get(name);
        if (text != null)
            return text;
        else
            return name;
    }

    /**
     * 设置参数文字
     *
     * @param name 参数名
     * @param text 文字
     */
    public void setArgText(String name, String text) {
        argText.put(name, text);
    }

    public abstract String OperatorName();

    @Override
    public String toString() {


        try {
            Map<String, Class> args = this.getArgs();
            if(args.size() == 0)
                return  this.OperatorName();

            String str = this.OperatorName() + " {";
            Iterator<Map.Entry<String, Class>> it = args.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Class> tmp = it.next();
                str += this.getArgText(tmp.getKey()) + "=" + this.getArg(tmp.getKey());
                if (it.hasNext())
                    str += ", ";
            }
            str += "}";
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return  this.OperatorName() + "-" + e.toString();
        }

    }
}
