%%Prep input
imw = 400;
imh = 500;
path = strrep(mfilename('fullpath'),mfilename,'');
for i=1:20
    url = [path '\tickets\im' num2str(i) '.jpg'];
    img = imread(url);
    B = imresize(img,[imh imw]);
    imwrite(B,[path '\tickets\imp' num2str(i) '.jpg']);
end
%% Load X and bin it
tic;
numimg = 20;
X=zeros(imh*imw,numimg);
for i=1:numimg
    url = [path '\tickets\imp' num2str(i) '.jpg'];
    img = rgb2gray(imread(url));
    img = adaptThres(img,20,5);
    X(:,i) = img(:);
    imshow(img);
    pause
end
toc;
% %% Process
% %X=[0 1;0 1];
% ins=size(X,1);
% l1=300;
% l2=100;
% w1=randn(l1,ins); %row_num = neuronas out, col_num neuronas in
% w2=randn(l2,l1);
% alfa=0.5;
% for r=1:20
%     x=X(:,r);
%     out=sigmoid(w1*x);
%     w1=w1+alfa*(x*2-1);
%     for i=1:length(out)
%         for j=1:length(x)
%             if(out(i)>0.5)
%                 w1(i,j)=w1(i,j)+alfa*(x(j)*2-1);
%             else
%                 w1(i,j)=w1(i,j)-alfa*(x(j)*2-1);
%             end
%         end
%     end
%     x=out;
%     out=sigmoid(w2*x);
%     for i=1:length(out)
%         for j=1:length(x)
%             if(out(i)>0.5)
%                 w2(i,j)=w2(i,j)+alfa*(x(j)*2-1);
%             else
%                 w2(i,j)=w2(i,j)-alfa*(x(j)*2-1);
%             end
%         end
%     end
% end
% sigmoid(w2*sigmoid(w1*x))
% 
% 
