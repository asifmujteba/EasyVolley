# EasyVolley
Android Volley Wrapper library to make networking easy, flexible and better.

<h2>Purpose</h2>
In 2013 google introduced Volley: Easy & Fast Networking library for Android. Since then I fell in love with Volley. It is indeed fast, easy and highly customizable But the approach to integrate it within a project for example, loading images, extending requests, customizing requests etc all seemed quite confusing to me. I always thought of how can we make it easy to use and easy to custimze P.S. also adding some new features would be nice :). This thought became the inspiration behind EasyVolley.

<h2>Installation</h2>

**GRADLE**

````java
dependencies {
  compile 'com.github.asifmujteba:easyvolley:0.9.+'
}
````
<h2>Usage</h2>
A Sample is already included to demonstrate the usage.

- Initialize and do global configurations using `EasyVolley` class once on application start and dispose in the end, preferably by extending Applcation class.

````java
EasyVolley.initialize(getApplicationContext());
````

````java
EasyVolley.dispose();
````

**Performing Http Requests**
````java
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

````java
EasyVolley.withGlobalQueue()
                .load(url)
                .asBitmap()
                .into(IMAGE_VIEW_HERE)
                .start();
````

**Managing Request Queues**

By default Easy Volley creates a global Request Queue to process all requests. We can also create a new request queue if needed.

````java
EasyVolley.withNewQueue(getApplicationContext())
````




<h2>Author</h2>
Asif Mujteba, asifmujteba@gmail.com
Twitter: @asifmujteba

<h2>License</h2>
This library is available under the Apache v2 license. See the LICENSE file for more info.
