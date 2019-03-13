# SwipeBack
An android library that can finish a activity by using gesture.

# Using help
Let your activity exnteds `SwipeBackActivity` and setting style as Translucent and `windowAnimationStyle` set null, but you also custom windowAnimationStyle 

**Such as**
``` Java
  public class MainActivity extends SwipeBackActivity{
      @Override
      protect void onCreate(saveInstanceState Bundle){
          super.onCreate(saveInstanceState);
          setContentView(R.layout.activity_main);
      }
  }
```

`declared styles.xml`
``` XML
<style name="AppTheme.Translucent" parent="AppTheme">
  <item name="android:windowBackground">@android:color/transparent</item>
  <item name="android:windowIsTranslucent">true</item>
  <item name="android:windowAnimationStyle">@null</item>
</style>
```


# Next step
- [ ] Support fragment
