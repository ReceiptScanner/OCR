function [ out ] = adaptThres( in,s,t )
%ADAPTTHRES Summary of this function goes here
%   Detailed explanation goes here
%   in : greyscale image. 2 dimensions!
%   s: window size used for averages
%   t: thresholding percentage
in = im2double(in);
w = size(in,1);
h = size(in,2);
intImg = im2double(zeros(size(in)));
out = zeros(size(in));
for i = 1 : w
    sum = 0;
    for j = 1 : h
        sum = sum + in(i,j);
        if i == 1
            intImg(i,j)=sum;
        else
            intImg(i,j) = intImg(i-1,j)+sum;
        end
    end
end

for i = 1 : w
    for j = 1 : h
        x1 = i - s/2;
        x2 = i + s/2;
        y1 = j - s/2;
        y2 = j + s/2;
        x1 = inBounds(x1,w);
        x2 = inBounds(x2,w);
        y1 = inBounds(y1,h);
        y2 = inBounds(y2,h);
        count = (x2-x1)*(y2-y1);
        sum = intImg(x2-1,y2-1)-intImg(x2-1,y1)-intImg(x1,y2-1)+intImg(x1,y1);
        if (in(i,j)*count) <= (sum*(100-t)/100)
            out(i,j)=0;
        else
            out(i,j)=255;
        end
    end
end
end

function y=inBounds(x,wh)
    if x > wh
        y = wh;
    elseif x < 1
        y = 1;
    else
        y = x;
    end
end