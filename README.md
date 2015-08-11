# EasyVolley
Android Volley Wrapper library to make networking easy, flexible and better.

<h2>Purpose</h2>
In 2013 google introduced Volley: Easy & Fast Networking library for Android. Since then I fell in love with Volley. It is indeed fast, easy and highly customizable But the approach to integrate it within a project for example, loading images, extending requests, customizing requests etc all seemed quite confusing to me. I always thought of how can we make it easy to use and easy to custimze P.S. also adding some new features would be nice :). This thought became the inspiration behind EasyVolley.

<h2>Installation</h2>

**GRADLE**

````
dependencies {
  compile 'com.github.asifmujteba:easyvolley:0.9.+'
}
````
<h2>Usage</h2>
A Sample is already included to demonstrate the usage.

- Initialize and do global configurations using `EasyVolley` class once on application start and dispose in the end, preferably by extending Applcation class.

````
EasyVolley.initialize(getApplicationContext());
````

````
EasyVolley.dispose();

````

**Performing Http Requests**

````
EasyVolley.withGlobalQueue()
                .load(URL_COMES_HERE)
                .asJsonObject()
                .setSubscriber(OBJECT_REFERENCE_HERE) // Once this object unsubscribes the request is cancelled and no callbacks are called
                .setCallback(new ASFRequestListener<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject response) {
                        // DO_SOMETHING_HERE
                    }
                    
                    @Override
                    public void onFailure(Exception e) {
                        // SO_SOMETHING_HERE
                    }
                })
                .start();
````


**Subscription Mechanism**

Call `.setSubscriber(OBJECT_REFERENCE_HERE)` on a request to attach it against the object passed. i-e, Pass the activity here.

And call `EasyVolley.unSubscribe(OBJECT_REFERENCE_HERE);` when the request is longer needed i-e, In onDestroy() method of Activity.

This will ensure that requests are cancelled and callbacks are not called after the activity has been destroyed.


**Performing Image Requests**
- Ability to set callbacks on image load or directly load it into an ImageView
- Ability to set image width, height and different scale types.

````
EasyVolley.withGlobalQueue()
                .load(url)
                .asBitmap()
                .setMaxWidth(WIDTH_HERE) // OPTIONAL
                .setMaxHeight(HEIGHT_HERE) // OPTIONAL
                .setScaleType(SCALE_TYPE_HERE) // OPTIONAL
                .setCallback(...) // OPTIONAL
                .into(IMAGE_VIEW_HERE) // OPTIONAL
                .start();
````

**Managing Request Queues**

By default EasyVolley creates a global Request Queue to process all requests. We can also create new request queues if needed.

````
EasyVolley.withNewQueue(getApplicationContext())
````

**Setting Headers**

````
EasyVolley.withGlobalQueue()
                .load(URL_COMES_HERE)
                .addHeader(KEY1_HERE, VALUE1_HERE)
                .addHeader(KEY2_HERE, VALUE2_HERE)
                ...
````

**Adding Get/Post Parameters**

````
EasyVolley.withGlobalQueue()
                .load(URL_COMES_HERE)
                .addParam(KEY1_HERE, VALUE1_HERE)
                .addParam(KEY2_HERE, VALUE2_HERE)
                ...
````
**Request Caching**
- Cache uses least recently used technique to limit memory size.
- By defaut all requests are cached in memory and disk as well. (caching http requests directly in memory in addition to images really fastens the User experience especially for fast scrolling scenarios)
- Http headers, Get parameters, Post body digest are all taken into consideration for effiecient caching

<h2>Coming Soon / TODO</h2>
- Schedule Callbacks on Main Thread always.
- Respecting Http Cache-Control and Expiry Headers for memory Cache as well.
- Auto scale images to fit ImageViews by inspecting ImageView attributes
- Treat same image requests with different scale sizes differently.
- Ability to Add http headers and parameters to all requests globally and for a specfic Request Queue.
- Ability to set Image loading configurations globally
- Loading and caching images from Content Providers and Local disk
- Multipart Http Requests
- Write Wrappers for upload/Download tasks
- Batch Requests
- By default set low priority for Image Requests
- Prioritize certain requests
 



<h2>Author</h2>
Asif Mujteba, asifmujteba@gmail.com
Twitter: @asifmujteba

<h2>License</h2>
This library is available under the Apache v2 license. See the LICENSE file for more info.
