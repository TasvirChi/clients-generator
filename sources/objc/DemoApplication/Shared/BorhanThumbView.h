//
//  BorhanThumbView.h
//  Borhan
//
//  Created by Pavel on 02.04.12.
//  Copyright (c) 2012 Borhan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BorhanThumbView : UIImageView {
    
    BorhanMediaEntry *mediaEntry;
    
    int width;
    int height;
    
    BOOL isLoading;
    ASIHTTPRequest *request;
}

- (void)updateWithMediaEntry:(BorhanMediaEntry *)_mediaEntry;
- (void)updateWithMediaEntry:(BorhanMediaEntry *)_mediaEntry withSize:(CGSize)size;

@property (nonatomic, assign) BorhanMediaEntry *mediaEntry;

@end
