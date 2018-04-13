import cv2  
import numpy as np  
import os  
import bottle
def fetch_face_pic(img,face_cascade):  
        # 将图像灰度化  
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)  
        # 人脸检测  
        #faces = face_cascade.detectMultiScale(gray,scaleFactor=1.1,minNeighbors=10,minSize=(30, 30),flags=1)
        faces,rl,wl = face_cascade.detectMultiScale3(gray,scaleFactor=1.1,
                minNeighbors=3,
                maxSize=(100,100),
                minSize=(50,50),
                flags = cv2.CASCADE_SCALE_IMAGE,
                outputRejectLevels = True
                )
        #print(faces,rl,wl)
        ol = 0
        #for (x, y, w, h) in faces:
        crop = None
        for i,(x,y,w,h) in enumerate(faces):
            #cv2.rectangle(img, (x, y), (x+w, y+h), (0, 255, 0), 2)  # 使用rectangle()可以绘出检测出的人脸区域  
            #print(i,x,y,w,h)
            j = 1 
            if wl[i][0] > ol: 
                crop = img[y:y+h, x:x+w] # 使用切片操作直接提取感兴趣的区域
                print(wl[i][0],ol,x,y,w,h)
                ol = wl[i][0]
        return ol,crop 


face_cascade = cv2.CascadeClassifier('/root/girl/opencv-master/data/haarcascades/haarcascade_frontalface_alt2.xml')

#path_jaffe = '/web/maps.cc/public/girl/img/'
'''
#遍历处理文件里所有人脸图像  
for file in os.listdir(path_jaffe):  
        jaffe_pic = os.path.join(path_jaffe,file)  
        img = cv2.imread(jaffe_pic)  
        crop = fetch_face_pic(img,face_cascade)  
        print(jaffe_pic)
        #cv2.imshow("Faces found", img)  
        #cv2.imshow('Crop image', crop)    
        #cv2.waitKey(0)  
        #cv2.destroyAllWindows()  
        # 将图像缩放到64*64大小  
        #resized_img=cv2.resize(crop,(64,64),interpolation=cv2.INTER_CUBIC)  
        #保存图像  
        if crop is not None:
            #print(type(crop))
            #if not crop
            print("has person")
            cv2.imwrite(jaffe_pic + "_m.jpg", crop)
'''
@bottle.route('/find/<w>', method='GET')
def do_find(w):
    jaffe_pic = '/web/maps.cc/public/girl/newimg/' + w
    img = cv2.imread(jaffe_pic)
    ol,crop = fetch_face_pic(img,face_cascade)
   
    if crop is not None:
        cv2.imwrite("/web/maps.cc/public/girl/newthumb/"+w,crop)
        return w

    return "" 


bottle.run(host='0.0.0.0', port=8080)  
